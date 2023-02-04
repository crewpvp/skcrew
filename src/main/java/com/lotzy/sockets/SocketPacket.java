package com.lotzy.sockets;

import java.io.Serializable;

public class SocketPacket implements Serializable {

    public enum PacketType {
        PLAYER_LEAVE,
        PLAYER_JOIN,
        SERVER_CONNECTED,
        SERVER_DISCONNECTED,
        CONNECTION_INFO,
        //next packets can send only from servers
        COMMAND,
        CONNECTION,
        ALIVE,
        SIGNAL
    }
    
    public PacketType type;
    public Object body;
    public String receiver = null;
    
    public static SocketPacket PlayerLeavePacket(String player, String from) {
        return new SocketPacket(PacketType.PLAYER_LEAVE, new String[] {player,from});
    }
    public static SocketPacket PlayerJoinPacket(String player, String to) {
        return new SocketPacket(PacketType.PLAYER_JOIN, new String[] {player,to});
    }
    
    public static SocketPacket ConnectionInfoPacket(String name, ServerInfo[] servers) {
        return new SocketPacket(PacketType.CONNECTION_INFO, new Object[] {name,servers});
    }
    public static SocketPacket ConnectionPacket(int port) {
        return new SocketPacket(PacketType.CONNECTION, port);
    }
    public static SocketPacket ServerConnected(String server) {
        return new SocketPacket(PacketType.SERVER_CONNECTED, server);
    }
    public static SocketPacket ServerDisconnected(String server) {
        return new SocketPacket(PacketType.SERVER_DISCONNECTED, server);
    }
    public static SocketPacket AlivePacket() {
        return new SocketPacket(PacketType.ALIVE, null);
    }
    
    public static SocketPacket SignalPacket(String server, String key,Object value) {
        return new SocketPacket(PacketType.SIGNAL,new Object[] {key,value}, server);
    }
    public static SocketPacket CommandPacket(String server, String[] commands) {
        return new SocketPacket(PacketType.COMMAND, commands, server);
    }
    
    public SocketPacket(PacketType type, Object body) {
        this.type = type;
        this.body = body;
    }
    public SocketPacket(PacketType type, Object body, String receiver) {
        this.type = type;
        this.body = body;
        this.receiver = receiver;
    }
    
}
