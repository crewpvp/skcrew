package com.lotzy.skcrew.shared.sockets.data;

import com.lotzy.skcrew.shared.sockets.packets.Packet;
import com.lotzy.skcrew.spigot.Skcrew;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.HashSet;

public class SpigotServer extends BaseServer implements Serializable {
    HashSet<SpigotPlayer> players;
    
    public SpigotServer(String name, InetSocketAddress address) {
        super(name, address);
        this.players = new HashSet();
    }
    
    public SpigotServer(String name, InetSocketAddress address, HashSet<SpigotPlayer> players) {
        super(name, address);
        players.forEach(player -> player.setServer(this));
    }
    
    @Override
    public void setDisconnected() {
        super.setDisconnected();
        this.players.clear();
    }
    
    @Override
    public void setConnected(boolean connected) {
        super.setConnected(connected);
        if (!connected) this.players.clear();
    }
    
    public HashSet<SpigotPlayer> getPlayers() {
        return this.players;
    }
    
    public void sendPacket(Packet packet) {
        if (this.isConnected())
            Skcrew.getInstance().getSocketClient().sendObject(packet);
    }
    
}
