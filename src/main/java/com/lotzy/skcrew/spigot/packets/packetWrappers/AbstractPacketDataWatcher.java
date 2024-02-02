package com.lotzy.skcrew.spigot.packets.packetWrappers;

import io.netty.buffer.ByteBuf;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface AbstractPacketDataWatcher {
    
    public Field getDataWatcherField();
     public void setDataWatcherField(Field dataWatcher);
    
}
