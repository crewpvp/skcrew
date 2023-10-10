package com.lotzy.skcrew.proxy;

import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.shared.sockets.data.BaseServer;
import com.lotzy.skcrew.shared.sockets.data.NetworkServer;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

public interface SkcrewImpl { 
    
    abstract public Path getDataDirectory();
    
    abstract public void info(String message);
    
    abstract public Set<NetworkServer> getAllServers();
    
    abstract public BasePlayer getPlayer(UUID uuid);
    
    abstract public BasePlayer getPlayer(String name);
    
    abstract public BaseServer getServer(String name);
    
    abstract public void switchPlayers(BasePlayer[] players, BaseServer server);

    abstract public void kickPlayers(BasePlayer[] playres, String reason);
}

