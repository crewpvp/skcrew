package com.lotzy.skcrew.shared.sockets.packets;

import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import java.util.HashSet;

public class PacketServersInfo extends Packet {
    String name;
    
    public PacketServersInfo(String currentName, HashSet<SpigotServer> servers) {
        super(PacketType.SERVERS_INFO, servers);
        this.name = currentName;
    }
    
    public HashSet<SpigotServer> getServers() {
        return (HashSet<SpigotServer>) this.data;
    }
    
    public String getName() {
        return this.name;
    }
    
}
