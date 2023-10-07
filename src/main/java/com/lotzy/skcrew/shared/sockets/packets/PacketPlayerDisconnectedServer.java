package com.lotzy.skcrew.shared.sockets.packets;

import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.shared.sockets.data.BaseServer;

public class PacketPlayerDisconnectedServer extends Packet {
    private BaseServer server;
    public PacketPlayerDisconnectedServer(BasePlayer player, BaseServer server) {
        super(PacketType.PLAYER_DISCONNECTED_SERVER, player);
        this.server = server;
    }
    
    public BasePlayer getPlayer() {
        return (BasePlayer) this.data;
    }
    
    public BaseServer getServer() {
        return this.server;
    }
    
}
