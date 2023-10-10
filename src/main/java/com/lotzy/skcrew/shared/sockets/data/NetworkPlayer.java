package com.lotzy.skcrew.shared.sockets.data;

import com.lotzy.skcrew.proxy.Skcrew;
import java.util.UUID;

public class NetworkPlayer extends BasePlayer {
    
    NetworkServer server = null;
    int join_date;
    
    public NetworkPlayer(String name, UUID uuid) {
        super(name,uuid);
        join_date = (int) (System.currentTimeMillis()/1000);
    }
    
    public NetworkPlayer(String name, UUID uuid, NetworkServer server) {
        super(name,uuid);
        this.server = server;
        if  (this.server != null) this.server.getPlayers().add(this);
        join_date = (int) (System.currentTimeMillis()/1000);
    }
   
    public NetworkServer getServer() {
        return this.server;
    }
    
    public int getJoinDate() {
        return this.join_date;
    }
    
    public int getTimePlayed() {
        return ((int)(System.currentTimeMillis()/1000)) - this.getJoinDate();
    }
    
    public void setServer(NetworkServer server) {
        if  (this.server != null) this.server.getPlayers().remove(this);
        this.server = server;
        if  (this.server != null) this.server.getPlayers().add(this);
    }
    
    public void kick() {
        Skcrew.getInstance().kickPlayers(new BasePlayer[] {this.toBasePlayer()}, null);
    }
    
    public void kick(String reason) {
        Skcrew.getInstance().kickPlayers(new BasePlayer[] {this.toBasePlayer()}, reason);
    }
    
    public void connect(NetworkServer server) {
        Skcrew.getInstance().switchPlayers(new BasePlayer[] {this.toBasePlayer()}, server);
    }
    
    @Override
    public String toString() {
        return "{ \"name\": \"" + this.getName() + "\", \"uuid\": \"" + this.getUUID().toString() + "\", \"join_date\": "+ this.getJoinDate() +", \"time_played\": "+ this.getTimePlayed() +", \"server_name\": \"" + this.server.getName() + "\" }";
    }

    public String toString(boolean write_server) {
        if (!write_server) this.toString();
        return "{ \"name\": \"" + this.getName() + "\", \"uuid\": \"" + this.getUUID().toString() + "\", \"join_date\": "+ this.getJoinDate() +", \"time_played\": "+ this.getTimePlayed() +", \"server_name\": \"" + this.server.getName() + 
        "\", \"server\": " + this.server.toString(true) + " }";
    }
    
}
