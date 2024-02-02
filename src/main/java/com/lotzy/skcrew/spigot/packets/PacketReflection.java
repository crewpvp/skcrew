package com.lotzy.skcrew.spigot.packets;

import com.lotzy.skcrew.spigot.packets.packetWrappers.ConstructorPacketMethod;
import com.lotzy.skcrew.spigot.packets.packetWrappers.ConstructorPacketMethodDataWatcher;
import com.lotzy.skcrew.spigot.packets.packetWrappers.ConstructorPacketDataWatcher;
import com.lotzy.skcrew.spigot.packets.packetWrappers.AbstractPacket;
import com.lotzy.skcrew.spigot.packets.packetWrappers.MethodPacket;
import com.lotzy.skcrew.spigot.packets.packetWrappers.BasePacket;
import com.lotzy.skcrew.spigot.packets.packetWrappers.MethodPacketDataWatcher;
import com.lotzy.skcrew.spigot.packets.packetWrappers.ConstructorPacket;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import org.bukkit.entity.Player;

public class PacketReflection {
    public static HashMap<String,AbstractPacket> PACKETS = new HashMap();
    public static HashSet<String> States = new HashSet();
    public static HashSet<String> Bounds = new HashSet();
    public static PacketReflection instance;
    
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
    
    public static Class ClientboundBundlePacketClass = null;
    public static Constructor ClientboundBundlePacketConstructor = null;
    public static Method ClientboundBundlePacketDecoder = null;
    
    public static Class BlockPositionClass = null;
    public static Constructor BlockPositionConstructor = null;
    public static Field[] BlockPositionFields = null;
    public static Method BlockPositionAsLongMethod = null;
    public static Method BlockPositionFromLongMethod = null;
    
    public static Class DataWatcherClass = null;
    public static Object DataWatcherInstance = null;
    
    public static Method GetGameProfileMethod = null;
    
    
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
        for (Package pkg : getPackages()) {
            if (!pkg.getName().startsWith("org.bukkit.craftbukkit")) continue;
	    if (!pkg.getName().endsWith(".entity")) continue;
            try {
                Class CraftPlayer = Class.forName(pkg.getName()+".CraftPlayer");
                CraftPlayerClass = CraftPlayer;
                GetHandleMethod = CraftPlayer.getMethod("getHandle");
                EntityPlayerClass = GetHandleMethod.getReturnType();
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private static void getGameProfileMethod() {
        try {
            GetGameProfileMethod = CraftPlayerClass.getMethod("getProfile");
        } catch (Exception ex) {
            
        }
    }
    
    private static void getPlayerConnectionClassAndField() {
        for(Field field : EntityPlayerClass.getDeclaredFields()) {
            if(!field.getType().getSimpleName().equals("PlayerConnection")) continue;
            field.setAccessible(true);
            PlayerConnectionField = field;
	    PlayerConnectionClass = field.getType();
            return;
        }
    }

    private static void getNetworkManagerClassAndField() {
        for(Field field : PlayerConnectionClass.getDeclaredFields()) {
            if(!field.getType().getSimpleName().equals("NetworkManager")) continue;
            field.setAccessible(true);
            NetworkManagerField = field;
	    NetworkManagerClass = field.getType();
            return;
        }
        for(Field field : PlayerConnectionClass.getSuperclass().getDeclaredFields()) {
            if(!field.getType().getSimpleName().equals("NetworkManager")) continue;
            field.setAccessible(true);
            NetworkManagerField = field;
	    NetworkManagerClass = field.getType();	
            return;
        }
    }

    private static void getChannelField() {
        for(Field field : NetworkManagerClass.getDeclaredFields()) {
            if(!field.getType().getSimpleName().equals("Channel")) continue;
            field.setAccessible(true);
            ChannelField = field;
            return;
        }
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
                if(!type.getSimpleName().equals("EnumProtocol")) continue;
                EnumProtocolClass = type;
                return;
            }
        }
        Class PacketListenerClass = null;
        for(Field field : NetworkManagerClass.getDeclaredFields()) {
            if (!field.getType().getSimpleName().equals("PacketListener")) continue;
            PacketListenerClass = field.getType();
            break;
        }
        if(PacketListenerClass == null) return;
        for(Method method : PacketListenerClass.getDeclaredMethods()) {
            if(!method.getReturnType().getSimpleName().equals("EnumProtocol")) continue;
            EnumProtocolClass = method.getReturnType();
            return;
        }
    }
    
    private static void getEnumProtocolDirectionClass() {
        for(Field field : NetworkManagerClass.getDeclaredFields()) {
            if(!field.getType().getSimpleName().equals("EnumProtocolDirection")) continue;
            EnumProtocolDirectionClass = field.getType();
            return;  
        }
    }
    
    private static void getPacketDataSerializerClass() {
        for(Method method : PacketClass.getDeclaredMethods()) {
            if(method.getParameterCount()!=1) continue;
            Class paramType = method.getParameterTypes()[0];
            if(!paramType.getSimpleName().equals("PacketDataSerializer")) continue;
            PacketDataSerializerClass = paramType;
            return;
        }
    }
    
    private static void getPacketDataSerializerConstructor() {
        for(Constructor constructor : PacketDataSerializerClass.getConstructors()) {
            if(constructor.getParameterCount() != 1) continue;
            if(!constructor.getParameterTypes()[0].equals(ByteBuf.class)) continue;
            PacketDataSerializerConstructor = constructor;
            return;
        }
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
        Class tBlockPositionClass = null;
        for(Method method : PacketDataSerializerClass.getDeclaredMethods()) {
            if(!method.getReturnType().getSimpleName().equals("BlockPosition")) continue;
            tBlockPositionClass = method.getReturnType();
            break;
        }
        if (tBlockPositionClass == null) return;
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
    
    private static void getPacketClasses() {
        Field statesMapField = null; 
        for(Field field : EnumProtocolClass.getDeclaredFields()) {
            if(Modifier.isStatic(field.getModifiers())) continue;
            if(!field.getType().isAssignableFrom(Map.class)) continue;
            field.setAccessible(true);
            statesMapField = field;
        }
        
        for(Object state : EnumProtocolClass.getEnumConstants()) {
            Map currentStatesMap;
            try {
                currentStatesMap = (Map)statesMapField.get(state);
            } catch (Exception ex) {
                continue;
            }
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
                    States.add(((Enum)state).name().toLowerCase());
                    Bounds.add(((Enum)bound).name().toLowerCase());
                    PACKETS.put(packet.getSimpleName(), new BasePacket(packet,id,((Enum)state).name().toLowerCase(),((Enum)bound).name().toLowerCase()));
                }
            } 
        }  
    }
    
    private static AbstractPacket getPacketEncoder(AbstractPacket packet) {
        Class cls = packet.getPacket();
        Field dataWatcherField = null;
        for(Field field : cls.getDeclaredFields()){
            if (!field.getType().getSimpleName().equals("DataWatcher")) continue;
            if(DataWatcherClass==null) {
                DataWatcherClass = field.getType();
                try {
                    DataWatcherClass.getConstructors()[0].newInstance(null);
                } catch (Exception ex) {
                    break;
                } 
            }
            dataWatcherField = field;
            break;
        }
        for(Constructor cnst : cls.getConstructors()) {
            if(cnst.getParameterCount() != 1) continue;
            if(!cnst.getParameterTypes()[0].equals(PacketDataSerializerClass)) continue;
            if(dataWatcherField!=null) {
                return ConstructorPacketDataWatcher.fromAbstractPacket(packet,cnst,dataWatcherField);
            }
            return ConstructorPacket.fromAbstractPacket(packet,cnst);
        }
	for(Method method : cls.getDeclaredMethods()){
            if(!Modifier.isStatic(method.getModifiers())) continue;
            if(method.getParameterCount() != 1) continue;
            if(!method.getParameterTypes()[0].equals(PacketDataSerializerClass)) continue;
            if(!method.getReturnType().equals(cls)) continue;
            if(dataWatcherField!=null) {
                return MethodPacketDataWatcher.fromAbstractPacket(packet,method,dataWatcherField);
            }
            return MethodPacket.fromAbstractPacket(packet,method);
        }
        Object packetInstance;
        Constructor constructor;
        try {
            constructor = cls.getConstructor();
            packetInstance = constructor.newInstance();
        } catch (Exception ex) {
            return packet;
        } 
        for(Method method : cls.getDeclaredMethods()){
            if (Modifier.isStatic(method.getModifiers())) continue;
            if (method.getParameterCount()!=1) continue;
            if (!method.getParameterTypes()[0].equals(PacketDataSerializerClass)) continue;
            try {
                method.invoke(packetInstance, PacketDataSerializerConstructor.newInstance(Unpooled.buffer()));      
            } catch ( InstantiationException | IllegalAccessException | IllegalArgumentException ex) {
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
                if (!method.getParameterTypes()[0].equals(PacketDataSerializerClass)) continue;
                if (method.equals(((ConstructorPacketMethod)packet).getMethod())) continue;
                packet.setDecoder(method);
                return packet;
            } 
        }
        for(Method method : cls.getDeclaredMethods()){
            if (Modifier.isStatic(method.getModifiers())) continue;
            if (method.getParameterCount()!=1) continue;
            if (!method.getParameterTypes()[0].equals(PacketDataSerializerClass)) continue;
            packet.setDecoder(method);
        }
        return packet;
    }
    
    private static void getBundlePacket() {
        if(!PACKETS.containsKey("ClientboundBundlePacket")) return;
        Class BundlePacketClass = PACKETS.get("ClientboundBundlePacket").getClass();
        for(Constructor constructor : BundlePacketClass.getConstructors()) {
            if(constructor.getParameterCount()!=1) continue;
            if(!constructor.getParameterTypes()[0].equals(Iterable.class)) continue;
            ClientboundBundlePacketConstructor = constructor;
            break;
        }
        for(Method method : BundlePacketClass.getSuperclass().getDeclaredMethods()) {
            if(method.getParameterCount()!=1) continue;
            if(!method.getReturnType().equals(Iterable.class)) continue;
            ClientboundBundlePacketDecoder = method;
            break;
        }
        if(ClientboundBundlePacketDecoder!=null && ClientboundBundlePacketConstructor != null)
            ClientboundBundlePacketClass = BundlePacketClass;
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
        insertHandlerInPipeline(player,"packet_handler","skcrew_packet_handler", new ChannelPacketHandler(player));
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
        for(Player player : players)
            for(Object packet : packets) {
                try {
                    SendPacketMethod.invoke(NetworkManagerField.get(PlayerConnectionField.get(GetHandleMethod.invoke(player))),packet);
                } catch (Exception ex) {
                    continue;
                }
            }
    }
    
    public static GameProfile getGameProfile(Player player) {
        try {
            return (GameProfile)GetGameProfileMethod.invoke(player);
        } catch (Exception ex) {
            return null;
        } 
    }
}
