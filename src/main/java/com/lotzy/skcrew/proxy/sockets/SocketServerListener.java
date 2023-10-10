package com.lotzy.skcrew.proxy.sockets;

import com.lotzy.skcrew.proxy.Skcrew;
import com.lotzy.skcrew.proxy.sockets.SocketServer.ServerListener;
import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.shared.sockets.data.BaseServer;
import com.lotzy.skcrew.shared.sockets.data.NetworkPlayer;
import com.lotzy.skcrew.shared.sockets.data.NetworkServer;
import com.lotzy.skcrew.shared.sockets.packets.Packet;
import com.lotzy.skcrew.shared.sockets.packets.PacketIncomingSignal;
import com.lotzy.skcrew.shared.sockets.packets.PacketInitialConnection;
import com.lotzy.skcrew.shared.sockets.packets.PacketKickPlayer;
import com.lotzy.skcrew.shared.sockets.packets.PacketOutcomingSignal;
import com.lotzy.skcrew.shared.sockets.packets.PacketPlayerConnectedProxy;
import com.lotzy.skcrew.shared.sockets.packets.PacketPlayerConnectedServer;
import com.lotzy.skcrew.shared.sockets.packets.PacketPlayerDisconnectedProxy;
import com.lotzy.skcrew.shared.sockets.packets.PacketPlayerDisconnectedServer;
import com.lotzy.skcrew.shared.sockets.packets.PacketServerConnected;
import com.lotzy.skcrew.shared.sockets.packets.PacketServerDisconnected;
import com.lotzy.skcrew.shared.sockets.packets.PacketServersInfo;
import com.lotzy.skcrew.shared.sockets.packets.PacketSwitchPlayer;
import com.lotzy.skcrew.shared.sockets.packets.PacketType;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SocketServerListener implements ServerListener {
    Set<InetAddress> machineAddresses;
    Set<NetworkServer> allowedServers;
    Set<SocketClientThread> pendingServers;
    int connection_timeout = 15000;
    
    public SocketServerListener(Set<NetworkServer> allowedServers) {
        this.allowedServers = allowedServers;
        this.pendingServers = new HashSet();
        this.machineAddresses = new HashSet<>();
        this.machineAddresses.addAll(SocketServer.getLocalV4Addresses());
        InetAddress externalIp = SocketServer.getExternalAddress(); 
        if (externalIp != null) this.machineAddresses.add(externalIp);
    }
    
    public void setConnectionTimeout(int millis) {
        this.connection_timeout = millis;
    }
    
    private boolean addressIsLocal(InetAddress address) {
        return this.machineAddresses.contains(address);
    }
    
    @Override
    public void onClientConnection(SocketClientThread client) {
        InetAddress clientAddress = client.getSocket().getInetAddress();
        Skcrew.getInstance().info("Client server: "+clientAddress.getHostAddress()+" trying to connect");
        for(NetworkServer server : this.getServers()) {
            if (server.isConnected()) continue;
            InetAddress serverAddress = SocketServer.getIpFromDomain(server.getHostName());
            if(serverAddress == null) continue;
            if (serverAddress.equals(clientAddress) || (addressIsLocal(clientAddress) && addressIsLocal(serverAddress))) {
                try {
                    client.getSocket().setSoTimeout(connection_timeout);
                } catch (SocketException ex) {}
                this.pendingServers.add(client);
                return;
            }
            client.close();
            Skcrew.getInstance().info("Client server: "+client.getSocket().getInetAddress().getHostAddress()+" disconnected because its not in the list of allowed servers");
        }
    }
    
    @Override
    public void onClientDisconnection(SocketClientThread client) {
        if (this.pendingServers.contains(client)) {
            this.pendingServers.remove(client);
            Skcrew.getInstance().info("Client server: "+client.getSocket().getInetAddress().getHostAddress()+" auto-disconnected because didn't send its port");
            return;
        }
        NetworkServer disconnectedServer = this.getServers().stream().filter(server -> server.getClient() == client).findFirst().orElse(null);
        if (disconnectedServer == null) return;
        Skcrew.getInstance().info("Client server: "+disconnectedServer.getName()+" ("+disconnectedServer.getIP()+":"+disconnectedServer.getPort()+") disconnected");
        disconnectedServer.setDisconnected();
        
        PacketServerDisconnected packet = new PacketServerDisconnected(disconnectedServer.toBaseServer());
        this.getOnlineServers().forEach(server -> server.sendPacket(packet));
    }
    
    @Override
    public void onReceivePacketFromClient(SocketClientThread client, Object object) {
        Packet packet;
        try {
            packet = (Packet) object;
            if (packet == null) return;
        } catch (ClassCastException e) {
            return;
        }
        
        if (this.pendingServers.contains(client)) {
            if (packet.getType() != PacketType.INITIAL_CONNECTION) {
                this.pendingServers.remove(client);
                client.close();
                Skcrew.getInstance().info("Client server: "+client.getSocket().getInetAddress().getHostAddress()+" disconnected because its send wrong packet");
                return;
            }
            int port = ((PacketInitialConnection) (packet)).getPort();
            NetworkServer connectedServer = this.getServers().stream().filter(server -> !server.isConnected() && server.getPort() == port).findFirst().orElse(null);
            if (connectedServer == null) {
                this.pendingServers.remove(client);
                client.close();
                Skcrew.getInstance().info("Client server: "+client.getSocket().getInetAddress().getHostAddress()+":"+port+" disconnected because its not in the list of allowed servers");
                return;
            }
            this.pendingServers.remove(client);
            try {
                client.getSocket().setSoTimeout(0);
            } catch (SocketException ex) {}
            connectedServer.setConnected(client);
            client.sendObject(new PacketServersInfo(connectedServer.getName(), this.allowedServers));
            PacketServerConnected packetServerConnected = new PacketServerConnected(connectedServer.toBaseServer(),connectedServer.getPlayers().stream().map(player -> player.toBasePlayer()).collect(Collectors.toCollection(HashSet::new)));
            this.getOnlineServers().stream().filter(server -> !server.equals(connectedServer)).forEach(server -> server.sendPacket(packetServerConnected));
            Skcrew.getInstance().info("Сlient server: "+connectedServer.getName()+" ("+connectedServer.getIP()+":"+connectedServer.getPort()+") connected");
            return;
        }
        
        switch (packet.getType()) {
            case INCOMING_SIGNAL: {
                PacketIncomingSignal ipacket = (PacketIncomingSignal) packet;
                PacketOutcomingSignal opacket = new PacketOutcomingSignal(ipacket.getSignals());
                for (BaseServer receiver : ipacket.getReceivers())
                    this.getServer(receiver).sendPacket(opacket);
                break;
            }
            case KICK_PLAYER: {
                PacketKickPlayer spacket = (PacketKickPlayer) packet;
                Skcrew.getInstance().kickPlayers(spacket.getPlayers(), spacket.getReason());
                break;
            }
            case SWITCH_PLAYER: {
                PacketSwitchPlayer spacket = (PacketSwitchPlayer) packet;
                Skcrew.getInstance().switchPlayers(spacket.getPlayers(), spacket.getServer());
                break;
            }
            default:
                break;
        }
    }
 
    public Set<NetworkServer> getServers() {
        return this.allowedServers;
    }
    public Set<NetworkServer> getOnlineServers() {
        return this.getServers().stream().filter(server -> server.isConnected()).collect(Collectors.toSet());
    }
    
    public NetworkServer getServer(BaseServer server) {
        return this.getServers().stream().filter(nserver -> nserver.equals(server)).findFirst().orElse(null);
    }
    
    public NetworkServer getServer(String name) {
        return this.getServers().stream().filter(server -> server.getName().equals(name)).findFirst().orElse(null);
    }
    
    public Set<NetworkPlayer> getPlayers() {
        return this.getOnlineServers().stream()
            .map(server -> server.getPlayers()).flatMap(players -> players.stream()).collect(Collectors.toSet());
    }
    
    public Set<NetworkPlayer> getPlayers(Set<BaseServer> servers) {
         return this.getOnlineServers().stream().filter(server -> servers.contains(server))
            .map(server -> server.getPlayers()).flatMap(players -> players.stream()).collect(Collectors.toSet());
    }
    
    public Set<NetworkPlayer> getPlayers(BaseServer server) {
        return this.getOnlineServers().stream().filter(fserver -> fserver.equals(server))
            .map(fserver -> fserver.getPlayers()).flatMap(players -> players.stream()).collect(Collectors.toSet());
    }
    
    public NetworkPlayer getPlayer(BasePlayer player) {
        return this.getPlayers().stream().filter(pplayer -> player.equals(pplayer)).findFirst().orElse(null);
    }
    
    public NetworkPlayer getPlayer(String name) {
        return this.getPlayers().stream().filter(pplayer -> pplayer.getName().equals(name)).findFirst().orElse(null);
    }
    
    public NetworkPlayer getPlayer(UUID uuid) {
       return this.getPlayers().stream().filter(pplayer -> pplayer.getUUID().equals(uuid)).findFirst().orElse(null);
    }
    
    public void onPlayerConnectedProxy(BasePlayer player) {
        PacketPlayerConnectedProxy packet = new PacketPlayerConnectedProxy(player);
        this.getOnlineServers().forEach(server -> server.sendPacket(packet));
    }
    
    public void onPlayerDisconnectedServer(BasePlayer player, BaseServer server) {
        PacketPlayerDisconnectedServer packet = new PacketPlayerDisconnectedServer(player, server);
        this.getOnlineServers().forEach(sserver -> sserver.sendPacket(packet)); 
        NetworkPlayer networkPlayer = this.getPlayer(player);
        if (networkPlayer != null) networkPlayer.setServer(null);
    }
    
    public void onPlayerSwitchServer(BasePlayer player, BaseServer oldServer, BaseServer newServer) {
        PacketPlayerDisconnectedServer disconnectPacket = new PacketPlayerDisconnectedServer(player, oldServer);
        this.getOnlineServers().forEach(sserver -> sserver.sendPacket(disconnectPacket));
        NetworkPlayer networkPlayer = this.getPlayer(player);
        NetworkServer networkServer = this.getServer(newServer);
        if (networkPlayer != null) networkPlayer.setServer(networkServer);
        PacketPlayerConnectedServer connectPacket = new PacketPlayerConnectedServer(player,newServer);
        this.getOnlineServers().forEach(sserver -> sserver.sendPacket(connectPacket));
    }
    
    public void onPlayerConnectedServer(BasePlayer player, BaseServer server) {
        PacketPlayerConnectedServer packet = new PacketPlayerConnectedServer(player,server);
        this.getOnlineServers().forEach(sserver -> sserver.sendPacket(packet));
        NetworkServer networkServer = this.getServer(server);
        NetworkPlayer networkPlayer = this.getPlayer(player);
        if (networkPlayer == null) networkPlayer = new NetworkPlayer(player.getName(),player.getUUID());
        networkPlayer.setServer(networkServer);
    }
    
    public void onPlayerDisconnectedProxy(BasePlayer player) {
        PacketPlayerDisconnectedProxy packet = new PacketPlayerDisconnectedProxy(player);
        this.getOnlineServers().forEach(sserver -> sserver.sendPacket(packet));
    }
}
