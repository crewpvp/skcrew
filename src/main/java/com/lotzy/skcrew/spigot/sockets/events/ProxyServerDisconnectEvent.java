package com.lotzy.skcrew.spigot.sockets.events;

import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ProxyServerDisconnectEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private final SpigotServer server;
    
    @Override
    public HandlerList getHandlers() {
        return this.handlers;
    }
   
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public ProxyServerDisconnectEvent(SpigotServer server) {
        this.server = server;
    }
    public SpigotServer getServer() {
        return this.server;
    }
    
}
