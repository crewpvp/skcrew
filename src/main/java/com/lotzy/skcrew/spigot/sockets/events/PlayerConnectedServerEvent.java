package com.lotzy.skcrew.spigot.sockets.events;

import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerConnectedServerEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private final OfflinePlayer player;
    private final SpigotServer server;
    
    @Override
    public HandlerList getHandlers() {
        return this.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
        
    public PlayerConnectedServerEvent(OfflinePlayer player, SpigotServer server) {
        this.player = player;
        this.server = server;
    }
    
    public OfflinePlayer getPlayer() {
        return this.player;
    }
    
    public SpigotServer getServer() {
        return this.server;
    }
}
