package com.lotzy.skcrew.shared.sockets.packets;

import com.lotzy.skcrew.shared.sockets.data.BasePlayer;

public class PacketPlayerConnectedProxy extends Packet {
    
    public PacketPlayerConnectedProxy(BasePlayer player) {
        super(PacketType.PLAYER_CONNECTED_PROXY, player);
    }
    
    public BasePlayer getPlayer() {
        return (BasePlayer) this.data;
    }
    
}
