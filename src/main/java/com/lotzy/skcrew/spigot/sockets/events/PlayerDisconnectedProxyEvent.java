package com.lotzy.skcrew.spigot.sockets.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDisconnectedProxyEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private final OfflinePlayer player;
    
    @Override
    public HandlerList getHandlers() {
        return this.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
        
    public PlayerDisconnectedProxyEvent(OfflinePlayer player) {
        this.player = player;
    }
    
    public OfflinePlayer getPlayer() {
        return this.player;
    }
}
