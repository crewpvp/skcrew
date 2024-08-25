package com.lotzy.skcrew.spigot.packets;

import com.lotzy.skcrew.spigot.packets.packetWrappers.ConstructorPacketMethod;
import com.lotzy.skcrew.spigot.packets.packetWrappers.ConstructorPacketMethodDataWatcher;
import com.lotzy.skcrew.spigot.packets.packetWrappers.ConstructorPacketDataWatcher;
import com.lotzy.skcrew.spigot.packets.packetWrappers.AbstractPacket;
import com.lotzy.skcrew.spigot.packets.packetWrappers.MethodPacket;
import com.lotzy.skcrew.spigot.packets.packetWrappers.BasePacket;
import com.lotzy.skcrew.spigot.packets.packetWrappers.MethodPacketDataWatcher;
import com.lotzy.skcrew.spigot.packets.packetWrappers.ConstructorPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class PacketReflection {
    private static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    public static String cbClass(String clazz) {
        return CRAFTBUKKIT_PACKAGE + "." + clazz;
    }

    public static ArrayList<Class<?>> PACKET_CLASSES = new ArrayList();
    public static HashMap<String,AbstractPacket> PACKETS = new HashMap();
    public static HashSet<String> States = new HashSet();
    public static HashSet<String> Bounds = new HashSet();
    public static PacketReflection instance;
    
    public static Function<?, ?> EMPTY_FUNCTION = input -> input;
    
    public static Class CraftPlayerClass = null;
    public static Method GetHandleMethod = null;
    public static Class EntityPlayerClass = null;
    public static Class PlayerConnectionClass = null;
    
    public static Field PlayerConnectionField = null;
    public static Class PlayerConnection = null;
    
    public static Field NetworkManagerField = null;
    public static Class NetworkManagerClass = null;
    
    public static Field ChannelField = null;
    
    public static Class PacketClass = null;
    public static Method SendPacketMethod = null;
    
    public static Class EnumProtocolClass = null;
    public static Class EnumProtocolDirectionClass = null;
    
    public static Class PacketDataSerializerClass = null;
    public static Constructor PacketDataSerializerConstructor = null;
    public static Field PacketDataSerializerByteBufField = null;
    public static ArrayList<String> PacketDataSerializerDeserializers = null;
    
    public static Class BundlePacketClass = null;
    public static Class ClientboundBundlePacketClass = null;
    public static Constructor BundlePacketConstructor = null;
    public static Method BundlePacketDecoderMethod = null;
    
    public static Class BlockPositionClass = null;
    public static Constructor BlockPositionConstructor = null;
    public static Field[] BlockPositionFields = null;
    public static Method BlockPositionAsLongMethod = null;
    public static Method BlockPositionFromLongMethod = null;
    
    public static Class DataWatcherClass = null;
    public static Object DataWatcherInstance = null;
    
    public static Method GetGameProfileMethod = null;
    public static Method GetPropertiesOfGameProfileMethod = null;
    public static Method GetPropertyFromPropertyMapMethod = null;
    public static Method PutPropertyToPropertyMapMethod = null;
    public static Class PropertyClass = null;
    public static Constructor PropertyConstructor = null;
    public static Field ValueOfPropertyField = null;
    public static Field SignatureOfPropertyField = null;
    
    public static Object IRegistry = null;
    public static Class<?> RegistryFriendlyByteBufClass = null;
    public static Constructor RegistryFriendlyByteBufConstructor = null;
    
    public static Function<ByteBuf,?> createFriendlyBuffer = null;
    
    public static void INITIATE() throws Exception {
        getEntityPlayerClass();
        if(EntityPlayerClass==null) throw new Exception("EntityPlayer class not matched");
        getPlayerConnectionClassAndField();
        if(PlayerConnectionField==null) throw new Exception("PlayerConnection field not matched");
        getNetworkManagerClassAndField();
        if(NetworkManagerField==null) throw new Exception("NetworkManager field not matched");
        getChannelField();
        if(ChannelField==null) throw new Exception("Channel field not matched");
        getPacketClassAndSendPacketMethod();
        if(PacketClass==null) throw new Exception("Packet class not matched");
        getEnumProtocolClass();
        if(EnumProtocolClass==null) throw new Exception("EnumProtocol class not matched");
        getEnumProtocolDirectionClass();
        if(EnumProtocolDirectionClass==null) throw new Exception("EnumProtocolDirection class not matched");
        getPacketDataSerializerClass();
        if(PacketDataSerializerClass==null) throw new Exception("PacketDataSerializer class not matched");
        getPacketDataSerializerConstructor();
        if(PacketDataSerializerConstructor==null) throw new Exception("PacketDataSerializer constructor not matched");
        createFriendlyBuffer = (ByteBuf buffer) -> { 
            try {
                return PacketReflection.PacketDataSerializerConstructor.newInstance(buffer);
            } catch (Exception ex) {
                return null;
            }
        };
        
        getIRegistry();
        if (IRegistry != null) {
            getRegistryFriendlyByteBuf();
            if (RegistryFriendlyByteBufConstructor != null) {
                createFriendlyBuffer = (ByteBuf buffer) -> {
                    try {
                        return PacketReflection.RegistryFriendlyByteBufConstructor.newInstance(buffer, IRegistry);
                    } catch (Exception ex) {
                        return null;
                    }
                };
            }
        }
        getPacketDataDeserializers();
        if(PacketDataSerializerDeserializers==null) throw new Exception("PacketDataSerializer deserializer methods not matched");
        getPacketDataSeriealizerButebufField();
        if(PacketDataSerializerByteBufField==null) throw new Exception("PacketDataSerializer Bytebuf field not matched");
        getBlockPositionClass();
        if(BlockPositionClass==null) throw new Exception("BlockPosition class not matched");
        getPacketClasses();
        if(PACKETS.isEmpty()) throw new Exception("Packets not found");
        for(Entry<String,AbstractPacket> entry : PACKETS.entrySet()) {
            AbstractPacket packet = getPacketDecoder(getPacketEncoder(entry.getValue()));
            PACKETS.put(entry.getKey(),packet); 
        }
        getBundlePacket();
        getGameProfileMethod();
    }
    
    public static Package[] getPackages() {
        try {
            Method method = ClassLoader.class.getDeclaredMethod("getPackages");
            method.setAccessible(true);
            return (Package[])(method.invoke(PacketReflection.class.getClassLoader()));
        } catch (Exception ex) {
            return Package.getPackages();
        }    
    }

    private static void getEntityPlayerClass() {
        try {
            CraftPlayerClass = Class.forName(cbClass("entity.CraftPlayer"));
            GetHandleMethod = CraftPlayerClass.getMethod("getHandle");
            EntityPlayerClass = GetHandleMethod.getReturnType();
        } catch (Exception ex) { }
    }
    
    private static void getGameProfileMethod() {
        try {
            GetGameProfileMethod = CraftPlayerClass.getMethod("getProfile");
            GetPropertiesOfGameProfileMethod = GetGameProfileMethod.getReturnType().getDeclaredMethod("getProperties");
            GetPropertiesOfGameProfileMethod.setAccessible(true);
            GetPropertyFromPropertyMapMethod = GetPropertiesOfGameProfileMethod.getReturnType().getMethod("get", Object.class);
            PutPropertyToPropertyMapMethod = GetPropertiesOfGameProfileMethod.getReturnType().getMethod("put", Object.class, Object.class);
            ParameterizedType parameterizedType = (ParameterizedType)GetPropertiesOfGameProfileMethod.getReturnType().getGenericSuperclass();
            PropertyClass = (Class)parameterizedType.getActualTypeArguments()[1];
            PropertyConstructor = PropertyClass.getConstructor(String.class,String.class,String.class);
            ValueOfPropertyField = PropertyClass.getDeclaredField("value");
            ValueOfPropertyField.setAccessible(true);
            SignatureOfPropertyField = PropertyClass.getDeclaredField("signature");
            SignatureOfPropertyField.setAccessible(true);
        } catch (Exception ex) {}
    }
    
    private static void getPlayerConnectionClassAndField() {
        Field field = PowerfulReflection.getFieldByTypeNames(EntityPlayerClass, "PlayerConnection", "ServerGamePacketListenerImpl");
        if (field == null) return;
        PlayerConnectionField = field;
        PlayerConnectionClass = field.getType();
    }

    private static void getNetworkManagerClassAndField() {
        Field field = PowerfulReflection.getFieldByTypeNames(PlayerConnectionClass, "NetworkManager", "Connection");
        if (field == null) field = PowerfulReflection.getFieldByTypeNames(PlayerConnectionClass.getSuperclass(), "NetworkManager", "Connection");
        if (field == null) return;
        NetworkManagerField = field;
        NetworkManagerClass = field.getType();
    }

    private static void getChannelField() {
        Field field = PowerfulReflection.getFieldByTypeNames(NetworkManagerClass, "Channel");
        if (field == null) return;
        ChannelField = field;
    }
    
    private static void getPacketClassAndSendPacketMethod() {
        for(Method method : NetworkManagerClass.getDeclaredMethods()) {
            if(method.getParameterCount() != 1) continue;
            Class paramType = method.getParameterTypes()[0];
            if(!paramType.getSimpleName().equals("Packet")) continue;
            PacketClass = paramType;
            method.setAccessible(true);
            SendPacketMethod = method;
            return;
        }
    }
    
    private static void getEnumProtocolClass() {
        for(Method method : NetworkManagerClass.getDeclaredMethods()) {
            for(Class type : method.getParameterTypes()) {
                if(!(type.getSimpleName().equals("EnumProtocol") || type.getSimpleName().equals("ConnectionProtocol"))) continue;
                EnumProtocolClass = type;
                return;
            }
        }
        Field field = PowerfulReflection.getFieldByTypeNames(NetworkManagerClass, "PacketListener");
        if (field == null) return;
        Class PacketListenerClass = field.getType();
        Method method = PowerfulReflection.getMethodByReturnTypeNames(PacketListenerClass, "EnumProtocol","ConnectionProtocol");
        if (method == null) return;
        EnumProtocolClass = method.getReturnType();
    }
    
    private static void getEnumProtocolDirectionClass() {
        Field field = PowerfulReflection.getFieldByTypeNames(NetworkManagerClass, "EnumProtocolDirection", "PacketFlow");
        if (field == null) return;
        EnumProtocolDirectionClass = field.getType();
    }
    
    private static void getPacketDataSerializerClass() {
        for(Method method : PacketClass.getDeclaredMethods()) {
            if(method.getParameterCount()!=1) continue;
            Class paramType = method.getParameterTypes()[0];
            if(!(paramType.getSimpleName().equals("PacketDataSerializer") || paramType.getSimpleName().equals("FriendlyByteBuf"))) continue;
            PacketDataSerializerClass = paramType;
            return;
        }
        try {
            Class CraftWorld = Class.forName(cbClass("CraftWorld"));
            Class WorldServer = CraftWorld.getMethod("getHandle").getReturnType();
            Class Chunk = null;
            for(Method method : WorldServer.getDeclaredMethods()) {
               if(method.getParameterCount()==0) continue;
               Class paramType = method.getParameterTypes()[0];
               if(!(paramType.getSimpleName().equals("Chunk") || paramType.getSimpleName().equals("LevelChunk"))) continue;
               Chunk = paramType;
            }
            if (Chunk == null) return;
            for(Method method : Chunk.getDeclaredMethods()) {
                if(method.getParameterCount()==0) continue;
                Class paramType = method.getParameterTypes()[0];
                if(!(paramType.getSimpleName().equals("PacketDataSerializer") || paramType.getSimpleName().equals("FriendlyByteBuf"))) continue;
                PacketDataSerializerClass = paramType;
                return;
            }
        } catch (Exception e) {}
    }
    
    private static void getPacketDataSerializerConstructor() {
        for(Constructor constructor : PacketDataSerializerClass.getConstructors()) {
            if(constructor.getParameterCount() != 1) continue;
            if(!constructor.getParameterTypes()[0].equals(ByteBuf.class)) continue;
            PacketDataSerializerConstructor = constructor;
            return;
        }
    }
    
    private static void getIRegistry() {
        Server server = Bukkit.getServer();
        try {
            Method getServerMethod = server.getClass().getDeclaredMethod("getServer");
            getServerMethod.setAccessible(true);
            Object nmsServer = getServerMethod.invoke(server);
            Method getIRegistryMethod = PowerfulReflection.getMethodByReturnTypeNames(nmsServer.getClass().getSuperclass(), "Frozen", "Dimension");
            getIRegistryMethod.setAccessible(true);
            IRegistry = getIRegistryMethod.invoke(nmsServer);
        } catch (Exception ex) {}
    }
    
    private static void getRegistryFriendlyByteBuf() {
        RegistryFriendlyByteBufClass = PowerfulReflection.getClassByNames("net.minecraft.network.RegistryFriendlyByteBuf");
        if (RegistryFriendlyByteBufClass == null) return;
        RegistryFriendlyByteBufConstructor = RegistryFriendlyByteBufClass.getDeclaredConstructors()[0];
    }
    
    private static void getPacketDataDeserializers() {
        ArrayList<String> decoders = new ArrayList();
        for(Method method : PacketClass.getDeclaredMethods()) {
            if(method.getParameterCount()!=1) continue;
            Class paramType = method.getParameterTypes()[0];
            if(!paramType.equals(PacketDataSerializerClass)) continue;
            decoders.add(method.getName());
        }
        PacketDataSerializerDeserializers = decoders;
    }
    
    private static void getPacketDataSeriealizerButebufField() {
        for(Field field : PacketDataSerializerClass.getDeclaredFields()) {
            if(!field.getType().equals(ByteBuf.class)) continue;
            field.setAccessible(true);
            PacketDataSerializerByteBufField = field;
            return;
        }
    }
    
    private static void getBlockPositionClass() {
        Method getBlockPosMethod = PowerfulReflection.getMethodByReturnTypeNames(PacketDataSerializerClass, "BlockPosition", "BlockPos");
        if (getBlockPosMethod == null) return;
        Class tBlockPositionClass = getBlockPosMethod.getReturnType();
        
        int i = 0;
        ArrayList<Field> fields = new ArrayList();
        
        for(Field field : tBlockPositionClass.getSuperclass().getDeclaredFields()) {
            if(Modifier.isStatic(field.getModifiers())) continue;
            if(!field.getType().equals(int.class)) continue;
            field.setAccessible(true);
            fields.add(field);
        }
        
        Method tBlockPositionFromLongMethod = null;
        for(Method method : tBlockPositionClass.getDeclaredMethods()) {
            if(!Modifier.isStatic(method.getModifiers())) continue;
            if(!method.getReturnType().equals(tBlockPositionClass)) continue;
            if(method.getParameterCount()!=1) continue;
            if(!method.getParameterTypes()[0].equals(long.class)) continue;
            method.setAccessible(true);
            tBlockPositionFromLongMethod = method;
        }
        if (tBlockPositionFromLongMethod == null) return;
        
        Method tBlockPositionAsLongMethod = null;
        for(Method method : tBlockPositionClass.getDeclaredMethods()) {
            if(Modifier.isStatic(method.getModifiers())) continue;
            if(!method.getReturnType().equals(long.class)) continue;
            method.setAccessible(true);
            tBlockPositionAsLongMethod = method;   
        }
        if (tBlockPositionAsLongMethod == null) return;
        
        Constructor tBlockPositionConstructor;
        try {
            tBlockPositionConstructor = tBlockPositionClass.getConstructor(int.class,int.class,int.class);
        } catch (Exception ex) {
            return;
        }
        Object blockPositionObject;
        try {
            blockPositionObject = tBlockPositionConstructor.newInstance(1,2,3);
        } catch (Exception ex) {
            return;
        }
        int check = 0;
        Field[] tBlockPositionFields = new Field[3];
        for (Field field : fields) {
            try {
                Object objectValue = field.get(blockPositionObject);
                if (objectValue == null) continue;
                int value = (int)objectValue;
                switch(value) {
                    case 1:
                        check++;
                        tBlockPositionFields[0] = field;
                        break;
                    case 2:
                        check++;
                        tBlockPositionFields[1] = field;
                        break;
                    case 3:
                        check++;
                        tBlockPositionFields[2] = field;
                        break;
                }
            } catch (Exception ex) {
                continue;
            }
        }
        if(check != 3) return;
        BlockPositionClass = tBlockPositionClass;
        BlockPositionFields = tBlockPositionFields;
        BlockPositionConstructor = tBlockPositionConstructor;
        BlockPositionAsLongMethod = tBlockPositionAsLongMethod;
        BlockPositionFromLongMethod = tBlockPositionFromLongMethod;
    }
    
    private static boolean getPacketsFor1_20_4() {
        Field statesMapField = null; 
        for(Field field : EnumProtocolClass.getDeclaredFields()) {
            if(Modifier.isStatic(field.getModifiers())) continue;
            if(!field.getType().isAssignableFrom(Map.class)) continue;
            field.setAccessible(true);
            statesMapField = field;
        }
        if (statesMapField == null) return false;
        for(Object state : EnumProtocolClass.getEnumConstants()) {
            Map currentStatesMap;
            try {
                currentStatesMap = (Map)statesMapField.get(state);
            } catch (Exception ex) { continue; }
            for(Object bound : EnumProtocolDirectionClass.getEnumConstants()) {
                Map<Object,Object> finalMap = null;
                Object currentBoundsMap = null;
                try {
                    currentBoundsMap = currentStatesMap.get(bound);
                } catch (Exception ex) {
                    continue;
                }
                if(currentBoundsMap==null) continue;
                String className = currentBoundsMap.getClass().getSimpleName();
                if (className.equals("Object2IntMap") || className.equals("HashBiMap")) {
                    finalMap = (Map)currentBoundsMap;
                } else {
                    outerloop:
                    for(Field cfield : currentBoundsMap.getClass().getDeclaredFields()) {
                        if(cfield.getType().getSimpleName().equals("Object2IntMap")) {
                            cfield.setAccessible(true);
                            try {
                                finalMap = (Map) cfield.get(currentBoundsMap);
                            } catch (Exception ex) {
                                continue;
                            }
                            break;
                        }
                        cfield.setAccessible(true);
                        Object insideMap;
                        try {
                            insideMap = cfield.get(currentBoundsMap);
                        } catch (Exception ex) {
                            continue;
                        } 
                        for (Field bfield : insideMap.getClass().getDeclaredFields()) {
                            if(bfield.getType().getSimpleName().equals("Object2IntMap")) {
                                bfield.setAccessible(true);
                                try {
                                    finalMap = (Map) bfield.get(insideMap);
                                } catch (Exception ex) {
                                    continue;
                                } 
                                break outerloop;
                            }
                        }
                    }	
                }
                if(finalMap == null) continue;
                for(Entry<Object,Object> entry : finalMap.entrySet()) {
                    Class packet;
                    int id;
                    if(entry.getKey() instanceof Integer) {
                        id = (int)entry.getKey();
                        packet = (Class)entry.getValue();
                    } else {
                        packet = (Class)entry.getKey();
                        id = (int)entry.getValue();
                    }
                    PACKET_CLASSES.add(packet);
                    States.add(((Enum)state).name().toLowerCase());
                    Bounds.add(((Enum)bound).name().toLowerCase());
                    PACKETS.put(packet.getSimpleName(), new BasePacket(packet,id,((Enum)state).name().toLowerCase(),((Enum)bound).name().toLowerCase()));
                }
            } 
        }
        return true;
    }
    
    private static boolean getPacketsFor1_20_5() {
        String[] protocolClassNames = new String[]{
            "net.minecraft.network.protocol.configuration.ConfigurationProtocols",
            "net.minecraft.network.protocol.game.GameProtocols",
            "net.minecraft.network.protocol.handshake.HandshakeProtocols",
            "net.minecraft.network.protocol.login.LoginProtocols",
            "net.minecraft.network.protocol.status.StatusProtocols"
        };
        
        Class<?>[] protocolClasses = PowerfulReflection.getClassesByNames(protocolClassNames);
        if (protocolClasses.length == 0) return false;
        Class<?> protocolInfoUnboundClass = PowerfulReflection.getClassByNames("net.minecraft.network.ProtocolInfo$a", "net.minecraft.network.ProtocolInfo$Unbound");
        if (protocolInfoUnboundClass == null) return false;
        Class<?> streamCodecClass = PowerfulReflection.getClassByNames("net.minecraft.network.codec.StreamCodec");
        if (streamCodecClass == null) return false;
        Class<?> idDispatchCodecClass = PowerfulReflection.getClassByNames("net.minecraft.network.codec.IdDispatchCodec");
        if (idDispatchCodecClass == null) return false;
        Class<?> idDispatchCodecEntryClass = PowerfulReflection.getClassByNames("net.minecraft.network.codec.IdDispatchCodec$Entry", "net.minecraft.network.codec.IdDispatchCodec$b");
        if (idDispatchCodecEntryClass == null) return false;
        Class<?> protocolInfoClass = PowerfulReflection.getClassByNames("net.minecraft.network.ProtocolInfo");
        if (protocolInfoClass == null) return false;
        Method bindCodecFunctionMethod = PowerfulReflection.getMethodByReturnTypes(protocolInfoUnboundClass, protocolInfoClass);
        if (bindCodecFunctionMethod == null) return false;
        Method codecOfProtocolInfoMethod = PowerfulReflection.getMethodByReturnTypes(protocolInfoClass, streamCodecClass);
        if (codecOfProtocolInfoMethod == null) return false;
        Field toIdField = PowerfulReflection.getFieldByTypeNames(idDispatchCodecClass, "Object2IntMap");
        if (toIdField == null) return false;
        Class<?> packetTypeClass = PowerfulReflection.getClassByNames("net.minecraft.network.protocol.PacketType");
        if (packetTypeClass == null) return false;
        
        HashMap<Object, Object[]> packetIdMap = new HashMap();
        for(Class protocolClass : protocolClasses) {
            for(Field field : protocolClass.getDeclaredFields()) {
                if (!Modifier.isFinal(field.getModifiers()) || !Modifier.isStatic(field.getModifiers())) continue;
                try {
                    Object protocolInfo = field.get(null);
                    if (!protocolInfoUnboundClass.isInstance(protocolInfo)) continue;
                    protocolInfo = bindCodecFunctionMethod.invoke(protocolInfo, EMPTY_FUNCTION);
                    if (!protocolInfoClass.isInstance(protocolInfo)) continue;
                    Method getStateMethod = PowerfulReflection.getMethodByReturnTypes(protocolInfoClass, EnumProtocolClass);
                    Object codec = codecOfProtocolInfoMethod.invoke(protocolInfo);
                    Object state = getStateMethod.invoke(protocolInfo);
                    Map<Object,Object> packetMap = (Map) toIdField.get(codec);
                    packetMap.entrySet().forEach(entry -> packetIdMap.put(entry.getKey(), new Object[] {entry.getValue(), state}));
                } catch (Exception e) { continue; }
            }
        }
        
        if (packetIdMap.isEmpty()) return false;
        String[] packetTypesClassNames = new String[] {
                "net.minecraft.network.protocol.common.CommonPacketTypes",
                "net.minecraft.network.protocol.configuration.ConfigurationPacketTypes",
                "net.minecraft.network.protocol.cookie.CookiePacketTypes",
                "net.minecraft.network.protocol.game.GamePacketTypes",
                "net.minecraft.network.protocol.handshake.HandshakePacketTypes",
                "net.minecraft.network.protocol.login.LoginPacketTypes",
                "net.minecraft.network.protocol.ping.PingPacketTypes",
                "net.minecraft.network.protocol.status.StatusPacketTypes"
        };
        Class<?>[] packetTypesClasses = PowerfulReflection.getClassesByNames(packetTypesClassNames);
        for(Class zpacketTypeClass : packetTypesClasses) {
            for(Field field : zpacketTypeClass.getDeclaredFields()) {
                Object packetType;
                try {
                    packetType = field.get(null);
                } catch (Exception ex) { continue; } 
                
                if (!packetTypeClass.isInstance(packetType)) continue;
                Class<?> packetClass = (Class<?>)(((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]);
                PACKET_CLASSES.add(packetClass);
                
                try {
                    Method getEnumProtocolDirection = PowerfulReflection.getMethodByReturnTypes(packetTypeClass,EnumProtocolDirectionClass);
                    Object bound = getEnumProtocolDirection.invoke(packetType);
                    Integer id = (Integer)(packetIdMap.get(packetType)[0]);
                    Object state = packetIdMap.get(packetType)[1];
                    
                    States.add(((Enum)state).name().toLowerCase());
                    Bounds.add(((Enum)bound).name().toLowerCase());
                    PACKETS.put(packetClass.getSimpleName(), new BasePacket(packetClass,id,((Enum)state).name().toLowerCase(),((Enum)bound).name().toLowerCase()));
                } catch (Exception ex) {
                    continue;
                }
            }
        }
        return true;
    }
    
    private static void getPacketClasses() {
        if (getPacketsFor1_20_5()) return;
        if (getPacketsFor1_20_4()) return;
    }
    
    private static AbstractPacket getPacketEncoder(AbstractPacket packet) {
        Class cls = packet.getPacket();
        Field dataWatcherField = null;
        for(Field field : cls.getDeclaredFields()){
            if (!(field.getType().getSimpleName().equals("DataWatcher") || field.getType().getSimpleName().equals("SynchedEntityData"))) continue;
            if(DataWatcherClass==null) {
                DataWatcherClass = field.getType();
                try {
                    DataWatcherClass.getDeclaredConstructors()[0].newInstance(null);
                } catch (Exception ex) {
                    break;
                } 
            }
            dataWatcherField = field;
            break;
        }
        for(Constructor cnst : cls.getDeclaredConstructors()) {
            if(cnst.getParameterCount() != 1) continue;
            if(!(cnst.getParameterTypes()[0].equals(PacketDataSerializerClass) || cnst.getParameterTypes()[0].equals(RegistryFriendlyByteBufClass))) continue;
            cnst.setAccessible(true);
            if(dataWatcherField!=null) {
                return ConstructorPacketDataWatcher.fromAbstractPacket(packet,cnst,dataWatcherField);
            }
            return ConstructorPacket.fromAbstractPacket(packet,cnst);
        }
	for(Method method : cls.getDeclaredMethods()){
            if(!Modifier.isStatic(method.getModifiers())) continue;
            if(method.getParameterCount() != 1) continue;
            if(!(method.getParameterTypes()[0].equals(PacketDataSerializerClass) || method.getParameterTypes()[0].equals(RegistryFriendlyByteBufClass))) continue;
            if(!method.getReturnType().equals(cls)) continue;
            if(dataWatcherField!=null) {
                return MethodPacketDataWatcher.fromAbstractPacket(packet,method,dataWatcherField);
            }
            return MethodPacket.fromAbstractPacket(packet,method);
        }
        Object packetInstance;
        Constructor constructor;
        try {
            constructor = cls.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            packetInstance = constructor.newInstance();
        } catch (Exception ex) {
            return packet;
        } 
        for(Method method : cls.getDeclaredMethods()){
            if (Modifier.isStatic(method.getModifiers())) continue;
            if (method.getParameterCount()!=1) continue;
            if (!(method.getParameterTypes()[0].equals(PacketDataSerializerClass) || method.getParameterTypes()[0].equals(RegistryFriendlyByteBufClass))) continue;
            try {
                method.invoke(packetInstance, createFriendlyBuffer.apply(Unpooled.buffer()));
                continue;
            } catch ( IllegalAccessException | IllegalArgumentException ex) {
                continue;
            } catch (InvocationTargetException ex ) {
                if(ex.getCause() == null) continue;
                if(!ex.getCause().getClass().equals(IndexOutOfBoundsException.class)) continue;
            }
            if(dataWatcherField!=null) {
                return ConstructorPacketMethodDataWatcher.fromAbstractPacket(packet,constructor,method,dataWatcherField);
            }
            return ConstructorPacketMethod.fromAbstractPacket(packet,constructor,method);
        }
        return packet;
    }
   
    private static AbstractPacket getPacketDecoder(AbstractPacket packet) {
        Class cls = packet.getPacket();
        if (packet instanceof ConstructorPacketMethod || packet instanceof ConstructorPacketMethodDataWatcher ) {
            for(Method method : cls.getDeclaredMethods()){
                if (Modifier.isStatic(method.getModifiers())) continue;
                if (method.getParameterCount()!=1) continue;
                if (!(method.getParameterTypes()[0].equals(PacketDataSerializerClass) || method.getParameterTypes()[0].equals(RegistryFriendlyByteBufClass))) continue;
                if (method.equals(((ConstructorPacketMethod)packet).getMethod())) continue;
                method.setAccessible(true);
                packet.setDecoder(method);
                return packet;
            } 
        }
        for(Method method : cls.getDeclaredMethods()){
            if (Modifier.isStatic(method.getModifiers())) continue;
            if (method.getParameterCount()!=1) continue;
            if (!(method.getParameterTypes()[0].equals(PacketDataSerializerClass) || method.getParameterTypes()[0].equals(RegistryFriendlyByteBufClass))) continue;
            method.setAccessible(true);
            packet.setDecoder(method);
        }
        return packet;
    }
    
    private static void getBundlePacket() {
        BundlePacketClass = PowerfulReflection.getClassByNames("net.minecraft.network.protocol.BundlePacket");
	if (BundlePacketClass == null) return;
        
        outerloop: for(Class<?> packet : PACKET_CLASSES) {
            Type[] itypes = packet.getGenericInterfaces();
            if(itypes.length != 1) continue;
            if (!(itypes[0] instanceof ParameterizedType)) continue;
            ParameterizedType ptype = (ParameterizedType)itypes[0]; 
            if (!ptype.getRawType().equals(PacketClass)) continue;
            Class packetListenerPlayOut = (Class)ptype.getActualTypeArguments()[0];
            if(!(packetListenerPlayOut.getSimpleName().equals("PacketListenerPlayOut") || packetListenerPlayOut.getSimpleName().equals("ClientGamePacketListener"))) continue;
            for(Method method : packetListenerPlayOut.getDeclaredMethods()) {
                if (method.getParameterCount()!=1) continue;
                Class cls = method.getParameterTypes()[0];
                if (!BundlePacketClass.isAssignableFrom(cls)) continue;
                ClientboundBundlePacketClass = cls;
                break outerloop;
            }
        }
        if (ClientboundBundlePacketClass == null) return;
        
        for(Constructor constructor : ClientboundBundlePacketClass.getDeclaredConstructors()) {
            if(constructor.getParameterCount()!=1) continue;
            if(!constructor.getParameterTypes()[0].equals(Iterable.class)) continue;
            constructor.setAccessible(true);
            BundlePacketConstructor = constructor;
            break;
        }
        
        for(Method method : BundlePacketClass.getDeclaredMethods()) {
            if(!method.getReturnType().equals(Iterable.class)) continue;
            BundlePacketDecoderMethod = method;
            break;
        }
    }
    
    public static Channel getChannelOfPlayer(Player player) {
        try {
            return (Channel)ChannelField.get(NetworkManagerField.get(PlayerConnectionField.get(GetHandleMethod.invoke(player))));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static void insertHandlerInPipeline(Player player, String before, String name, ChannelHandler handler) {
        try {
            Channel channel = (Channel)ChannelField.get(NetworkManagerField.get(PlayerConnectionField.get(GetHandleMethod.invoke(player))));
            channel.pipeline().addBefore(before, name, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static ChannelHandler getChannelHandler(Player player, String name) {
        try {
            Channel channel = (Channel)ChannelField.get(NetworkManagerField.get(PlayerConnectionField.get(GetHandleMethod.invoke(player))));
            return channel.pipeline().get(name);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static void injectHandlerIntoPlayerPipeline(Player player) {
        insertHandlerInPipeline(player,"packet_handler","skcrew_packet_handler", new ChannelPacketHandler(player, getChannelOfPlayer(player)));
    }
    
    public static ChannelPacketHandler getInjectedHandlerFromPlayer(Player player) {
        return (ChannelPacketHandler) getChannelHandler(player,"skcrew_packet_handler");
    }
    
    public static Object createPacket(String name, ByteBuf buffer) {
        AbstractPacket packet = PACKETS.get(name);
        return packet != null ? packet.createPacket(buffer) : null;
    }
    
    public static ByteBuf decodePacket(Object packet) {
        AbstractPacket abstractPacket = PACKETS.get(packet.getClass().getSimpleName());
        return abstractPacket != null ? abstractPacket.decodePacket(packet) : null;
    }
    
    public static void sendPacket(Collection<Player> players, Collection<Object> packets) {
        for(Player player : players) {
            for(Object packet : packets) {
                try {
                    SendPacketMethod.invoke(NetworkManagerField.get(PlayerConnectionField.get(GetHandleMethod.invoke(player))),packet);
                } catch (Exception ex) {
                    continue;
                }
            }
        }
    }
    
    public static void sendPacketWithoutEvent(Collection<Player> players, Collection<Object> packets) {
        for(Player player : players) {
            try {
                Channel channel = (Channel)ChannelField.get(NetworkManagerField.get(PlayerConnectionField.get(GetHandleMethod.invoke(player))));
                ChannelHandlerContext chc = channel.pipeline().context("skcrew_packet_handler");
                for(Object packet : packets) {
                    ChannelFuture channelfuture = chc.writeAndFlush(packet);
                    channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            } catch (Exception ex) {
                continue;
            }
        }
    }
    
    public static Object getGameProfile(Player player) {
        try {
            return GetGameProfileMethod.invoke(player);
        } catch (Exception ex) {
            return null;
        } 
    }
    
    public static Object getProperties(Object gameProfile) {
        try {
            return GetPropertiesOfGameProfileMethod.invoke(gameProfile);
        } catch (Exception ex) {
            return null;
        } 
    }
    
    public static boolean removeProperty(Object properties, Object property, String key) {
        try {
            Collection col = (Collection) GetPropertyFromPropertyMapMethod.invoke(properties,key);
            return col.remove(property);
        } catch (Exception ex) {
            return false;
        } 
    }
    
    public static Object getProperty(Object properties, String key) {
        try {
            Collection col = (Collection) GetPropertyFromPropertyMapMethod.invoke(properties,key);
            if (col.isEmpty()) {
                return null;
            }
            return col.iterator().next();
        } catch (Exception ex) {
            return null;
        } 
    }
    
    public static Object putProperty(Object properties,String key, Object property) {
        try {
            return PutPropertyToPropertyMapMethod.invoke(properties,key, property);
        } catch (Exception ex) {
            return null;
        } 
    }
    
    public static Object createProperty(String name, String value, String signature) {
         try {
            return PropertyConstructor.newInstance(name, value, signature);
        } catch (Exception ex) {
            return null;
        } 
    }
    
    public static String getValueOfProperty(Object property) {
        try {
            return (String)ValueOfPropertyField.get(property);
        } catch (Exception ex) {
            return null;
        } 
    }
    
    public static String getSignatureOfProperty(Object property) {
        try {
            return (String)SignatureOfPropertyField.get(property);
        } catch (Exception ex) {
            return null;
        } 
    }
}
