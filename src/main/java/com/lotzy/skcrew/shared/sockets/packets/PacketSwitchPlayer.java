package com.lotzy.skcrew.shared.sockets.packets;

import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.shared.sockets.data.BaseServer;
import java.util.Collection;

public class PacketSwitchPlayer extends Packet {
    BaseServer server;
    
    public PacketSwitchPlayer(BaseServer server, Collection<BasePlayer> players) {
        super(PacketType.SWITCH_PLAYER, players.toArray(new BasePlayer[0]));
        this.server = server;
    }
    
    public BasePlayer[] getPlayers() {
        return (BasePlayer[]) this.data;
    }
    
    public BaseServer getServer() {
        return this.server;
    }
     
}
