package com.lotzy.skcrew.spigot.packets.packetWrappers;

import io.netty.buffer.ByteBuf;
import java.lang.reflect.Method;

public interface AbstractPacket {
    
    public Class getPacket();
    
    public String getBound();
    
    public String getState();
    
    public int getID();
    
    public void setDecoder(Method decoder);
    
    public ByteBuf decodePacket(Object packet);
    
    public Method getDecoder();

    public Object createPacket(ByteBuf buffer);
    
    public void creationError();
    
}
