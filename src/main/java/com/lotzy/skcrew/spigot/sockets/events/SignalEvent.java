package com.lotzy.skcrew.spigot.sockets.events;

import com.lotzy.skcrew.shared.sockets.data.Signal;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SignalEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private final Signal signal;
    
    @Override
    public HandlerList getHandlers() {
        return this.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public SignalEvent(Signal signal) {
        this.signal = signal;
    }
    
    public Signal getSignal() {
        return this.signal;
    }
    
    public String getKey() {
        return this.signal.getKey();
    }
    
    public Object[] getData() {
        return this.signal.getData();
    }
}
