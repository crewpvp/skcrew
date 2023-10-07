package com.lotzy.skcrew.spigot.sockets.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ProxyReconnectEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return this.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
        
    public ProxyReconnectEvent() {
    }
}
