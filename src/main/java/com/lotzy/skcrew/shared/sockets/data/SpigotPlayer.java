package com.lotzy.skcrew.shared.sockets.data;

import java.io.Serializable;
import java.util.UUID;

public class SpigotPlayer extends BasePlayer implements Serializable  {
    SpigotServer server = null;
    
    
    public SpigotPlayer(String name, UUID uuid) {
        super(name,uuid);
    }
    
    public SpigotPlayer(String name, UUID uuid, SpigotServer server) {
        super(name,uuid);
        this.server = server;
        this.server.getPlayers().add(this);  
    }
   
    public SpigotServer getServer() {
        return this.server;
    }
    
    public void setServer(SpigotServer server) {
        this.server.getPlayers().remove(this);
        this.server = server;
        this.server.players.add(this);
        
    }
    
}
