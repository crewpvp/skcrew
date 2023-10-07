package com.lotzy.skcrew.shared.sockets.packets;

import com.lotzy.skcrew.shared.sockets.data.BaseServer;

public class PacketServerDisconnected extends Packet {
    
    public PacketServerDisconnected(BaseServer server) {
        super(PacketType.SERVER_DISCONNECTED, server);
    }
    
    public BaseServer getServer() {
        return (BaseServer) this.data;
    }
    
}
