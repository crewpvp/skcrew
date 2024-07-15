package com.lotzy.skcrew.spigot.packets.packetWrappers;

import com.lotzy.skcrew.spigot.packets.PacketReflection;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MethodPacketDataWatcher extends MethodPacket implements AbstractPacket,AbstractPacketDataWatcher {
    Field dataWatcherField = null;
    
    public MethodPacketDataWatcher(Class packet, int id, String state, String bound, Method method, Field dataWatcher) {
        super(packet, id, state, bound, method);
        setDataWatcherField(dataWatcher);
    }
    
    public static MethodPacket fromAbstractPacket(AbstractPacket packet, Method method, Field dataWatcher) {
        return new MethodPacketDataWatcher(packet.getPacket(),packet.getID(),packet.getState(),packet.getBound(),method, dataWatcher);
    }

    public void setDataWatcherField(Field dataWatcher) {
        if (dataWatcherField != null) return;
        this.dataWatcherField = dataWatcher;
        this.dataWatcherField.setAccessible(true);
    }
    
    public Field getDataWatcherField() {
        return this.dataWatcherField;
    }

    @Override
    public Object createPacket(ByteBuf buffer) {
        try {
            Object pbuffer = PacketReflection.createFriendlyBuffer.apply(buffer);
            Object packet = createMethod.invoke(null, pbuffer);
            dataWatcherField.set(packet, PacketReflection.DataWatcherInstance);
            return packet;
        } catch (Exception ex) {
            creationError();
            return null;
        }
    }  
}
