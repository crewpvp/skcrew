package com.lotzy.skcrew.shared.sockets.data;

import com.lotzy.skcrew.shared.sockets.packets.Packet;
import com.lotzy.skcrew.shared.sockets.packets.PacketIncomingSignal;
import com.lotzy.skcrew.shared.sockets.packets.PacketKickPlayer;
import com.lotzy.skcrew.shared.sockets.packets.PacketSwitchPlayer;
import com.lotzy.skcrew.spigot.Skcrew;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class SpigotServer extends BaseServer implements Serializable {
    HashSet<SpigotPlayer> players = new HashSet();
    
    public SpigotServer(String name, InetSocketAddress address) {
        super(name, address);
    }
    
    public SpigotServer(String name, InetSocketAddress address, HashSet<SpigotPlayer> players) {
        super(name, address);
        players.forEach(player -> player.setServer(this));    
    }
    
    public SpigotServer(String name, InetSocketAddress address, HashSet<SpigotPlayer> players, boolean connected) {
        super(name, address);
        super.setConnected(connected);
        players.forEach(player -> player.setServer(this));    
    }
    
    public BaseServer toBaseServer() {
        return new BaseServer(this.getName(),this.getInetSocketAddress(), this.isConnected());
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
    
    public void kick(Collection<SpigotPlayer> players, String reason) {
        this.sendPacket(new PacketKickPlayer(reason, players.stream().map(player -> player.toBasePlayer()).collect(Collectors.toSet())));
    }
    
    public void switchPlayers(Collection<SpigotPlayer> players) {
        this.sendPacket(new PacketSwitchPlayer(this.toBaseServer(),players.stream().map(player -> player.toBasePlayer()).collect(Collectors.toSet())));
    }
    
    public void sendSignal(Collection<Signal> signals) {
        this.sendPacket(new PacketIncomingSignal(Arrays.asList(this.toBaseServer()), signals));
    }
    
}
