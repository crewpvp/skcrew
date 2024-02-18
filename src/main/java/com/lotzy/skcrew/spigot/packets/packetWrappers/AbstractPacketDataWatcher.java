package com.lotzy.skcrew.spigot.packets.packetWrappers;

import java.lang.reflect.Field;

public interface AbstractPacketDataWatcher {
    
    public Field getDataWatcherField();
     public void setDataWatcherField(Field dataWatcher);
    
}
