package com.lotzy.skcrew.spigot.packets.packetWrappers;

import com.lotzy.skcrew.spigot.packets.PacketReflection;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Constructor;

public class ConstructorPacket extends BasePacket implements AbstractPacket {
    Constructor constructor;
    
    public ConstructorPacket(Class packet, int id, String state, String bound, Constructor constructor) {
        super(packet, id, state, bound);
        this.constructor = constructor;
    }
    
    public Constructor getConstructor() {
        return this.constructor;
    }
    
    public static ConstructorPacket fromAbstractPacket(AbstractPacket packet, Constructor constructor) {
        return new ConstructorPacket(packet.getPacket(),packet.getID(),packet.getState(),packet.getBound(),constructor);
    }
    
    @Override
    public Object createPacket(ByteBuf buffer) {
        try {
            Object pbuffer = PacketReflection.createFriendlyBuffer.apply(buffer);
            return constructor.newInstance(pbuffer);
        } catch (Exception ex) {
            creationError();
            return null;
        }
    }  
}
