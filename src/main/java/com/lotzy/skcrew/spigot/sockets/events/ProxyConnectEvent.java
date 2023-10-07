package com.lotzy.skcrew.spigot.sockets.events;

import java.net.InetSocketAddress;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ProxyConnectEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private final InetSocketAddress address;
    
    @Override
    public HandlerList getHandlers() {
        return this.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public ProxyConnectEvent(InetSocketAddress address) {
        this.address = address;
    }
    public InetSocketAddress getAddress() {
        return this.address;
    }
}
