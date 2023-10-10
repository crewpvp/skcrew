package com.lotzy.skcrew.shared.sockets.packets;

import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.shared.sockets.data.BaseServer;
import com.lotzy.skcrew.shared.sockets.data.NetworkServer;
import com.lotzy.skcrew.shared.sockets.data.SpigotPlayer;
import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class PacketServersInfo extends Packet {
    String name;
    
    public PacketServersInfo(String currentName, HashMap<BaseServer,HashSet<BasePlayer>> data) {
        super(PacketType.SERVERS_INFO, data);
        this.name = currentName;
    }
    
    public PacketServersInfo(String currentName, Collection<NetworkServer> servers) {
        super(PacketType.SERVERS_INFO);
        HashMap<BaseServer, HashSet<BasePlayer>> data = new HashMap();
        for(NetworkServer server : servers) {
            data.put(server.toBaseServer(), server.getPlayers().stream().map(player -> player.toBasePlayer()).collect(Collectors.toCollection(HashSet::new)));
        }
        this.data = data;
        this.name = currentName;
    }
    
    public HashSet<SpigotServer> getServers() {
        return ((HashMap<BaseServer,HashSet<BasePlayer>>) data).entrySet().stream().map(entry -> 
            new SpigotServer(entry.getKey().getName(),entry.getKey().getInetSocketAddress(), entry.getValue().stream().map(player -> 
                new SpigotPlayer(player.getName(),player.getUUID())).collect(Collectors.toCollection(HashSet::new))))
            .collect(Collectors.toCollection(HashSet::new));
    }
    
    public String getName() {
        return this.name;
    }
    
}
