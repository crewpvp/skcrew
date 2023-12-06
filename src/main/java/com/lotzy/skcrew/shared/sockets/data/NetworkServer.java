package com.lotzy.skcrew.shared.sockets.data;

import com.lotzy.skcrew.proxy.sockets.SocketClientThread;
import com.lotzy.skcrew.shared.sockets.packets.Packet;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.stream.Collectors;

public class NetworkServer extends BaseServer {
    
    private SocketClientThread connected = null;
    private HashSet<NetworkPlayer> players = new HashSet();;
    private int connectionDate;
    
    public NetworkServer(String name, InetSocketAddress address) {
        super(name, address);
        this.players = new HashSet();
        this.connectionDate = -1;
    }
    
    public NetworkServer(String name, InetSocketAddress address, HashSet<NetworkPlayer> players) {
        super(name, address);
        players.forEach(player -> player.setServer(this));
    }
    
    public int getConnectionDate() {
        return this.connectionDate;
    }
    
    public int getUptime() {
        if (this.getConnectionDate() < 0) return -1;
        return ((int)(System.currentTimeMillis()/1000)) - this.getConnectionDate();
    }
    
    @Override
    public boolean isConnected() {
        return connected !=null;
    }
    
    public void setConnected(SocketClientThread connected) {
        this.connected = connected;
        connectionDate = connected != null ? (int)(System.currentTimeMillis()/1000) : -1;
    }
    
    public SocketClientThread getClient() {
        return this.connected;
    }
    
    @Override
    public void setDisconnected() {
        this.connected = null;
        this.connectionDate = -1;
        
    }
    
    public HashSet<NetworkPlayer> getPlayers() {
        return this.players;
    }
    
    public void sendPacket(Packet packet) {
        if (this.isConnected())
            this.getClient().sendObject(packet);
    }
    
    public BaseServer toBaseServer() {
        return new BaseServer(this.getName(),this.getInetSocketAddress(), this.isConnected());
    }
    
    @Override
    public String toString() {
        return "{ \"name\": \"" + this.getName() + "\", \"address\": \"" + this.getIP() + "\", \"port\": " + this.getPort() + ", \"hostname\": \"" + this.getHostName() + 
                "\", \"online\": " + (this.isConnected() ? "true" : "false")+ ", \"connection_date\": " + this.getConnectionDate() + ", \"uptime\": " + this.getUptime() + ", \"players_count\": " + this.getPlayers().size() + " }";
    }
    
    public String toString(boolean write_players) {
        if (!write_players) return this.toString();
        String players = "[ " + String.join(",", this.getPlayers().stream().map(player -> player.toString()).collect(Collectors.toList())) + " ]";
        return "{ \"name\": \"" + this.getName() + "\", \"address\": \"" + this.getIP() + "\", \"port\": " + this.getPort() + ", \"hostname\": \"" + this.getHostName() + 
                "\", \"online\": " + (this.isConnected() ? "true" : "false") + ", \"connection_date\": " + this.getConnectionDate() + ", \"uptime\": " + this.getUptime() + ", \"players_count\": " + this.getPlayers().size() + ", \"players\": " + players + " }"; 
    }
    
}
