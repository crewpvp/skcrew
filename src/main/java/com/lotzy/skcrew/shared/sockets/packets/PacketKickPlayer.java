package com.lotzy.skcrew.shared.sockets.packets;

import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import java.util.Collection;

public class PacketKickPlayer extends Packet {
   
    String reason;
    
    public PacketKickPlayer(String reason, Collection<BasePlayer> players) {
        super(PacketType.KICK_PLAYER, players.toArray(new BasePlayer[0]));
        this.reason = reason;
    }
    
    public BasePlayer[] getPlayers() {
        return (BasePlayer[]) this.data;
    }
    
    public String getReason() {
        return this.reason;
    }
     
}
