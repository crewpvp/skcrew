package com.lotzy.skcrew.spigot.packets.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketEvent extends Event implements Cancellable {
    Player player;
    Object packet;
    
    private boolean isCancelled;
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    public PacketEvent(Player player, Object packet) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.packet = packet;
        this.isCancelled = false;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    @Override
    public HandlerList getHandlers() {
        return this.HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean bln) {
        isCancelled = bln;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Object getPacket() {
        return packet;
    }
    
    public void setPacket(Object packet) {
        this.packet = packet;
    }
    
}
