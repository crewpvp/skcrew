package com.lotzy.skcrew.spigot.sockets;

import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.skcrew.spigot.sockets.SocketClient.ClientListener;
import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.shared.sockets.data.BaseServer;
import com.lotzy.skcrew.shared.sockets.data.Utils;
import com.lotzy.skcrew.shared.sockets.data.SpigotPlayer;
import com.lotzy.skcrew.shared.sockets.data.Signal;
import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import com.lotzy.skcrew.spigot.sockets.events.PlayerConnectedProxyEvent;
import com.lotzy.skcrew.spigot.sockets.events.PlayerConnectedServerEvent;
import com.lotzy.skcrew.spigot.sockets.events.PlayerDisconnectedProxyEvent;
import com.lotzy.skcrew.spigot.sockets.events.PlayerDisconnectedServerEvent;
import com.lotzy.skcrew.spigot.sockets.events.ProxyConnectEvent;
import com.lotzy.skcrew.spigot.sockets.events.ProxyDisconnectEvent;
import com.lotzy.skcrew.spigot.sockets.events.ProxyReconnectEvent;
import com.lotzy.skcrew.spigot.sockets.events.ProxyServerConnectEvent;
import com.lotzy.skcrew.spigot.sockets.events.ProxyServerDisconnectEvent;
import com.lotzy.skcrew.spigot.sockets.events.SignalEvent;
import com.lotzy.skcrew.shared.sockets.packets.Packet;
import com.lotzy.skcrew.shared.sockets.packets.PacketType;
import com.lotzy.skcrew.shared.sockets.packets.PacketInitialConnection;
import com.lotzy.skcrew.shared.sockets.packets.PacketOutcomingSignal;
import com.lotzy.skcrew.shared.sockets.packets.PacketPlayerConnectedProxy;
import com.lotzy.skcrew.shared.sockets.packets.PacketPlayerConnectedServer;
import com.lotzy.skcrew.shared.sockets.packets.PacketPlayerDisconnectedProxy;
import com.lotzy.skcrew.shared.sockets.packets.PacketPlayerDisconnectedServer;
import com.lotzy.skcrew.shared.sockets.packets.PacketServerConnected;
import com.lotzy.skcrew.shared.sockets.packets.PacketServerDisconnected;
import com.lotzy.skcrew.shared.sockets.packets.PacketServersInfo;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;

public class SocketClientListener implements ClientListener {
    boolean isConnected = false;
    String name = null;
    Set<SpigotServer> servers = new HashSet();
    InetSocketAddress address = null;
    
    public SpigotServer getThisServer() {
        return this.servers.stream().filter(server -> server.getName().equals(this.name)).findFirst().orElse(null);
    }
    
    public Set<SpigotServer> getServers() {
        return this.servers;
    }
    public Set<SpigotServer> getOnlineServers() {
        return this.getServers().stream().filter(server -> server.isConnected()).collect(Collectors.toSet());
    }
    
    private SpigotServer getServer(BaseServer server) {
        return this.servers.stream().filter(spigotServer -> spigotServer.equals(server)).findFirst().orElse(null);
    }
    public SpigotServer getServer(String name) {
        return this.servers.stream().filter(server -> server.getName().equals(name)).findFirst().orElse(null);
    }
        
    public Set<SpigotPlayer> getPlayers() {
        return this.getOnlineServers().stream()
            .map(server -> server.getPlayers()).flatMap(players -> players.stream()).collect(Collectors.toSet());
    }
    public Set<SpigotPlayer> getPlayers(Collection<BaseServer> servers) {
        return this.getOnlineServers().stream().filter(server -> servers.contains(server))
            .map(server -> server.getPlayers()).flatMap(players -> players.stream()).collect(Collectors.toSet());
    }
    public Set<SpigotPlayer> getPlayers(BaseServer server) {
        return this.getOnlineServers().stream().filter(fserver -> fserver.equals(server))
            .map(fserver -> fserver.getPlayers()).flatMap(players -> players.stream()).collect(Collectors.toSet());
    }

    public SpigotPlayer getPlayer(BasePlayer player) {
        return this.getPlayers().stream().filter(pplayer -> player.equals(pplayer)).findFirst().orElse(null);
    }
    
    public SpigotPlayer getPlayer(String name) {
        return this.getPlayers().stream().filter(player -> player.getName().equals(name)).findFirst().orElse(null);
    }
    
    public SpigotPlayer getPlayer(UUID uuid) {
        return this.getPlayers().stream().filter(player -> player.getUUID().equals(uuid)).findFirst().orElse(null);
    }
    
    
    @Override
    public void onReconnection() {
        Bukkit.getScheduler().runTask(Skcrew.getInstance(),() -> Bukkit.getPluginManager().callEvent(new ProxyReconnectEvent()));
    }
    
    @Override
    public void onConnection(InetSocketAddress address) {
        this.address = address;
        Skcrew.getInstance().getSocketClient().sendObject(new PacketInitialConnection(Bukkit.getPort()));
    }
    
    @Override
    public void onDisconnection() {
        if (isConnected) {
            isConnected = false;
            Bukkit.getScheduler().runTask(Skcrew.getInstance(),() -> Bukkit.getPluginManager().callEvent(new ProxyDisconnectEvent(this.address)));
            this.servers.clear();
            return;
        }
    }
    
    public void sendPacket(Packet packet) {
        Skcrew.getInstance().getSocketClient().sendObject(packet);
    }
    
    @Override
    public void onReceivePacket(Object packet) {
        Packet p = (Packet) packet;
        if (!isConnected) {
            if (p.getType() != PacketType.SERVERS_INFO) return;
            PacketServersInfo infoPacket = (PacketServersInfo)p;
            this.servers = infoPacket.getServers().entrySet().stream().map(entry -> {
                BaseServer baseServer = entry.getKey();
                return new SpigotServer(baseServer.getName(), baseServer.getInetSocketAddress(), entry.getValue().stream().map(player -> 
                    new SpigotPlayer(player.getName(),player.getUUID())).collect(Collectors.toCollection(HashSet::new)), baseServer.isConnected());
            }).collect(Collectors.toSet());
            name = infoPacket.getName();
            Bukkit.getScheduler().runTask(Skcrew.getInstance(),() -> Bukkit.getPluginManager().callEvent(new ProxyConnectEvent(this.address)));
            isConnected = true;
            return;
        }
        
        switch (p.getType()) {
            case SERVER_CONNECTED:{
                PacketServerConnected packetServerConnected = (PacketServerConnected) packet;
                SpigotServer server = this.getServer(packetServerConnected.getServer());
                if (server == null) return;
                server.setConnected(true);
                Stream.of(packetServerConnected.getPlayers()).forEach(player -> new SpigotPlayer(player.getName(),player.getUUID(),server));
                Bukkit.getScheduler().runTask(Skcrew.getInstance(),() -> Bukkit.getPluginManager().callEvent(new ProxyServerConnectEvent(server)));
                break;
                }
            case SERVER_DISCONNECTED:{
                SpigotServer server = this.getServer(((PacketServerDisconnected) packet).getServer());
                if (server == null) return;
                server.setDisconnected();
                Bukkit.getScheduler().runTask(Skcrew.getInstance(),() -> Bukkit.getPluginManager().callEvent(new ProxyServerDisconnectEvent(server)));
                break;
                }
           
            case PLAYER_CONNECTED_PROXY:{
                BasePlayer player = ((PacketPlayerConnectedProxy) packet).getPlayer();
                Bukkit.getScheduler().runTask(Skcrew.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PlayerConnectedProxyEvent(Utils.BukkitPlayerFromBasePlayer(player))));
                break;
                }
            case PLAYER_DISCONNECTED_PROXY:{
                BasePlayer player = ((PacketPlayerDisconnectedProxy) packet).getPlayer();
                Bukkit.getScheduler().runTask(Skcrew.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PlayerDisconnectedProxyEvent(Utils.BukkitPlayerFromBasePlayer(player))));
                break;
                }
            
            case PLAYER_CONNECTED_SERVER:{
                BasePlayer player = ((PacketPlayerConnectedServer) packet).getPlayer();
                SpigotServer server = this.getServer(((PacketPlayerConnectedServer) packet).getServer());
                if (server == null) return;
                new SpigotPlayer(player.getName(),player.getUUID(),server);
                Bukkit.getScheduler().runTask(Skcrew.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PlayerConnectedServerEvent(Utils.BukkitPlayerFromBasePlayer(player), server)));
                break;
                }
            case PLAYER_DISCONNECTED_SERVER:{
                BasePlayer player = ((PacketPlayerDisconnectedServer) packet).getPlayer();
                SpigotServer server = this.getServer(((PacketPlayerDisconnectedServer) packet).getServer());
                if (server == null) return;
                server.getPlayers().remove(player);
                Bukkit.getScheduler().runTask(Skcrew.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PlayerDisconnectedServerEvent(Utils.BukkitPlayerFromBasePlayer(player), server)));
                break;
            }
            case OUTCOMING_SIGNAL:{
                PacketOutcomingSignal spacket = (PacketOutcomingSignal) packet;
                for (Signal signal : spacket.getSignals()) {
                    Bukkit.getScheduler().runTask(Skcrew.getInstance(), () -> Bukkit.getPluginManager().callEvent(new SignalEvent(signal)));
                }   
            }
            default:
                break;
        }
    }
}
