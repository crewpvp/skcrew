package com.lotzy.skcrew.proxy;

import com.lotzy.skcrew.proxy.sockets.SocketServer;
import com.lotzy.skcrew.proxy.sockets.SocketServerListener;
import com.lotzy.skcrew.proxy.sockets.WebServer;
import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.shared.sockets.data.BaseServer;
import com.lotzy.skcrew.shared.sockets.data.NetworkServer;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

public class Skcrew {
    static Skcrew instance = null;
    private final SkcrewImpl impl;
   
    private SocketServer socketServer;
    private SocketServerListener socketServerListener;
    private WebServer webServer;
    
    public Skcrew(SkcrewImpl impl) {
        Skcrew.instance = this;
        this.impl = impl;
        try {
            Config.UPDATE_CONFIG(this.getDataDirectory());
            this.socketServer = new SocketServer(Config.getSocketServerPort());
            this.socketServerListener = new SocketServerListener(this.getAllServers());
            this.socketServer.addListener(this.socketServerListener);
            if (!Config.isWebServerEnabled()) return;
            this.webServer = new WebServer(Config.getWebServerPort(), Config.getWebServerUser(), Config.getWebServerPassword());
        } catch (IOException ex) {}
    }
    
    public static Skcrew getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }
    
    public Path getDataDirectory() {
        return this.impl.getDataDirectory();
    }
    
    public SocketServer getSocketServer() {
        return this.socketServer;
    }  
    
    public SocketServerListener getSocketServerListener() {
        return this.socketServerListener;
    }
    
    public WebServer getWebServer() {
        return this.webServer;
    }
    
    public void info(String message) {
        this.impl.info(message);
    }

    public BasePlayer getPlayer(UUID uuid) {
        return this.impl.getPlayer(uuid);
    }
    
    public BasePlayer getPlayer(String name) {
        return this.impl.getPlayer(name);
    }
    
    public BaseServer getServer(String name) {
        return this.impl.getServer(name);
    }
    
    public void switchPlayers(BasePlayer[] players, BaseServer server) {
        this.impl.switchPlayers(players, server);
    }
    
    public void kickPlayers(BasePlayer[] playres, String reason) {
        this.impl.kickPlayers(playres, reason);
    }
    
    public Set<NetworkServer> getAllServers() {
        return this.impl.getAllServers();
    }
}
