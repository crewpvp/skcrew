package com.lotzy.skcrew.spigot.packets.packetWrappers;

import com.lotzy.skcrew.spigot.packets.PacketReflection;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ConstructorPacketMethodDataWatcher extends ConstructorPacketMethod implements AbstractPacket,AbstractPacketDataWatcher {
    Field dataWatcherField = null;
    
    public ConstructorPacketMethodDataWatcher(Class packet, int id, String state, String bound, Constructor constructor, Method method, Field dataWatcher) {
        super(packet, id, state, bound, constructor, method);
        setDataWatcherField(dataWatcher);
    }
    
    public static ConstructorPacketMethodDataWatcher fromAbstractPacket(AbstractPacket packet, Constructor constructor, Method method,Field dataWatcher) {
        return new ConstructorPacketMethodDataWatcher(packet.getPacket(),packet.getID(),packet.getState(),packet.getBound(),constructor,method, dataWatcher);
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
            Object packet = constructor.newInstance();
            writeMethod.invoke(packet, pbuffer);
            dataWatcherField.set(packet, PacketReflection.DataWatcherInstance);
            return packet;
        } catch (Exception ex) {
            creationError();
            return null;
        }
    }  
}
