package com.lotzy.skcrew.shared.sockets.packets;

import com.lotzy.skcrew.shared.sockets.data.BasePlayer;

public class PacketPlayerDisconnectedProxy extends Packet {
    
    public PacketPlayerDisconnectedProxy(BasePlayer player) {
        super(PacketType.PLAYER_DISCONNECTED_PROXY, player);
    }
    
    public BasePlayer getPlayer() {
        return (BasePlayer) this.data;
    }
    
}
