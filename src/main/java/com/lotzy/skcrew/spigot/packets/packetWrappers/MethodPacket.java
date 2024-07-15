package com.lotzy.skcrew.spigot.packets.packetWrappers;

import com.lotzy.skcrew.spigot.packets.PacketReflection;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Method;

public class MethodPacket extends BasePacket implements AbstractPacket {
    Method createMethod = null;
    
    public MethodPacket(Class packet, int id, String state, String bound, Method method) {
        super(packet, id, state, bound);
        createMethod = method;
    }
    
    public static MethodPacket fromAbstractPacket(AbstractPacket packet, Method method) {
        return new MethodPacket(packet.getPacket(),packet.getID(),packet.getState(),packet.getBound(),method);
    }
    
    public Method getMethod() {
        return this.createMethod;
    }

    @Override
    public Object createPacket(ByteBuf buffer) {
        try {
            Object pbuffer = PacketReflection.createFriendlyBuffer.apply(buffer);
            return createMethod.invoke(null, pbuffer);
        } catch (Exception ex) {
            creationError();
            return null;
        }
    }  
}
