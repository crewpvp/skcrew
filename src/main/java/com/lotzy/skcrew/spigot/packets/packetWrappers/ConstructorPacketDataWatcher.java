package com.lotzy.skcrew.spigot.packets.packetWrappers;

import com.lotzy.skcrew.spigot.packets.PacketReflection;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ConstructorPacketDataWatcher extends ConstructorPacket implements AbstractPacket,AbstractPacketDataWatcher {
    Field dataWatcherField = null;
    
    public ConstructorPacketDataWatcher(Class packet, int id, String state, String bound, Constructor constructor, Field dataWatcher) {
        super(packet, id, state, bound, constructor);
        setDataWatcherField(dataWatcher);
    }
    
    public static ConstructorPacketDataWatcher fromAbstractPacket(AbstractPacket packet, Constructor constructor, Field dataWatcher) {
        return new ConstructorPacketDataWatcher(packet.getPacket(),packet.getID(),packet.getState(),packet.getBound(),constructor, dataWatcher);
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
            Object packet = constructor.newInstance(pbuffer);
            dataWatcherField.set(packet, PacketReflection.DataWatcherInstance);
            return packet;
        } catch (Exception ex) {
            creationError();
            return null;
        }
    }  
}
