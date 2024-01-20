package com.lotzy.skcrew.shared.sockets.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class SpigotPlayer extends BasePlayer implements Serializable  {
    SpigotServer server = null;
    
    
    public SpigotPlayer(String name, UUID uuid) {
        super(name,uuid);
    }
    
    public SpigotPlayer(String name, UUID uuid, SpigotServer server) {
        super(name,uuid);
        this.server = server;
        if (this.server != null) this.server.getPlayers().add(this);  
    }
    
    public OfflinePlayer toOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.getUUID());
    }
   
    public SpigotServer getServer() {
        return this.server;
    }
    
    public void setServer(SpigotServer server) {
        if (this.server != null) this.server.getPlayers().remove(this);
        this.server = server;
        if (this.server != null) this.server.players.add(this);
    }
    
    public void kick() {
        if (this.server != null)
            this.server.kick(Arrays.asList(this), null);
    }
    
    public void kick(String reason) {
        if (this.server != null)
            this.server.kick(Arrays.asList(this), reason);
    }
    
    public void switchServer(SpigotServer newServer) {
        newServer.switchPlayers(Arrays.asList(this));
    }  
}
