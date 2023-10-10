package com.lotzy.skcrew.shared.sockets.packets;

import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.shared.sockets.data.BaseServer;
import java.util.Collection;

public class PacketServerConnected extends Packet {
    
    BasePlayer[] players;
    
    public PacketServerConnected(BaseServer server, Collection<BasePlayer> players) {
        super(PacketType.SERVER_CONNECTED, server);
        this.players = players.toArray(new BasePlayer[0]);
    }
    
    public BaseServer getServer() {
        return (BaseServer) this.data;
    }
    
    public BasePlayer[] getPlayers() {
        return this.players;
    }
    
}
