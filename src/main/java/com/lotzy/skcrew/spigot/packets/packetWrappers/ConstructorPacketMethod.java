package com.lotzy.skcrew.spigot.packets.packetWrappers;

import com.lotzy.skcrew.spigot.packets.PacketReflection;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ConstructorPacketMethod extends ConstructorPacket implements AbstractPacket {
    public Method writeMethod;
    
    public ConstructorPacketMethod(Class packet, int id, String state, String bound, Constructor constructor, Method method) {
        super(packet, id, state, bound, constructor);
        writeMethod = method;
    }
    
    public static ConstructorPacketMethod fromAbstractPacket(AbstractPacket packet, Constructor constructor, Method method) {
        return new ConstructorPacketMethod(packet.getPacket(),packet.getID(),packet.getState(),packet.getBound(),constructor,method);
    }
    
    public Method getMethod() {
        return this.writeMethod;
    }
    
    public Object rawPacket() {
        try {
            return constructor.newInstance();
        } catch (Exception ex) {
            return null;
        }
    }
    @Override
    public Object createPacket(ByteBuf buffer) {
        try {
            Object pbuffer = PacketReflection.createFriendlyBuffer.apply(buffer);
            Object packet = constructor.newInstance();
            writeMethod.invoke(packet, pbuffer);
            return packet;
        } catch (Exception ex) {
            creationError();
            return null;
        }
    }  
}
