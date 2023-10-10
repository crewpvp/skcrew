package com.lotzy.skcrew.bungeecord;

import com.lotzy.skcrew.proxy.Skcrew;
import com.lotzy.skcrew.proxy.SkcrewImpl;
import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.shared.sockets.data.BaseServer;
import com.lotzy.skcrew.shared.sockets.data.NetworkServer;
import com.lotzy.skcrew.shared.sockets.data.Utils;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.event.EventHandler;

public class Bungeecord extends Plugin implements SkcrewImpl,Listener  {

    private Path dataDirectory;
    private ProxyServer server;
    private Skcrew skcrew;
    
    @Override
    public void onEnable() {
        this.server = this.getProxy();
        this.dataDirectory = this.getDataFolder().toPath();
        this.skcrew = new Skcrew(this);
        this.server.getScheduler().runAsync(this, this.skcrew.getSocketServer());
        this.server.getScheduler().runAsync(this, this.skcrew.getWebServer());
        this.server.getPluginManager().registerListener(this, this);
    }
    
    @Override
    public void onDisable() {
        if (this.skcrew.getWebServer() != null) this.skcrew.getWebServer().close();
        if (this.skcrew.getSocketServer() != null) this.skcrew.getSocketServer().close();
    }
    
    public ProxyServer getProxyServer() {
        return this.server;
    }

    @Override
    public Path getDataDirectory() {
        return this.dataDirectory;
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        if (this.skcrew.getSocketServerListener() == null) return;
        BasePlayer player = Utils.BasePlayerFromBungeecordPlayer(event.getPlayer());
        BaseServer newserver = Utils.BaseServerFromBungeecordServer(event.getPlayer().getServer().getInfo());
        if (event.getFrom() == null) {
            this.skcrew.getSocketServerListener().onPlayerConnectedProxy(player);
            this.skcrew.getSocketServerListener().onPlayerConnectedServer(player, newserver);
        } else {
            BaseServer server = Utils.BaseServerFromBungeecordServer(event.getFrom());
            this.skcrew.getSocketServerListener().onPlayerSwitchServer(player, server, newserver);
        } 
    }
    
    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        if (this.skcrew.getSocketServerListener() == null) return;
        BasePlayer player = Utils.BasePlayerFromBungeecordPlayer(event.getPlayer());
        if (event.getPlayer().getServer() != null) {
            BaseServer server = Utils.BaseServerFromBungeecordServer(event.getPlayer().getServer().getInfo());
            this.skcrew.getSocketServerListener().onPlayerDisconnectedServer(player, server);
        }  
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        if (this.skcrew.getSocketServerListener() == null) return;
        BasePlayer player = Utils.BasePlayerFromBungeecordPlayer(event.getPlayer());
        if (event.getPlayer().getServer() !=null) {
            BaseServer server = Utils.BaseServerFromBungeecordServer(event.getPlayer().getServer().getInfo());
            this.skcrew.getSocketServerListener().onPlayerDisconnectedServer(player, server);
        }
        this.skcrew.getSocketServerListener().onPlayerDisconnectedProxy(player);
    }

    @Override
    public void info(String message) {
        this.getLogger().info(message);
    }

    @Override
    public BasePlayer getPlayer(UUID uuid) {
        ProxiedPlayer player = this.getProxyServer().getPlayer(uuid);
        if (player==null) return null;
        return Utils.NetworkPlayerFromBungeecordPlayer(player);
    }
    
    @Override
    public BasePlayer getPlayer(String name) {
        ProxiedPlayer player = this.getProxyServer().getPlayer(name);
        if (player==null) return null;
        return Utils.NetworkPlayerFromBungeecordPlayer(player);
    }
    
    @Override
    public BaseServer getServer(String name) {
        ServerInfo server = this.getProxyServer().getServerInfo(name);
        if (server== null) return null;
        return Utils.NetworkServerFromBungeecordServer(server);
    }
    
    @Override
    public void switchPlayers(BasePlayer[] players, BaseServer server) {
        ServerInfo serverInfo = this.getProxyServer().getServerInfo(server.getName());
        if (serverInfo == null) return;
        for (BasePlayer basePlayer : players) {
            ProxiedPlayer player = this.getProxyServer().getPlayer(basePlayer.getUUID());
            if (player==null) continue;
            player.connect(serverInfo);
        }
        
    }
    
    @Override
    public void kickPlayers(BasePlayer[] players, String reason) {
        BaseComponent component = reason != null ? ComponentSerializer.parse(reason)[0] : new TextComponent("");
        for (BasePlayer p : players) {
            ProxiedPlayer c = this.getProxyServer().getPlayer(p.getUUID());
            if (c!=null) c.disconnect(component);
        }
    }
    
    @Override
    public Set<NetworkServer> getAllServers() {
        return this.getProxyServer().getServers().values().stream().map(server -> Utils.NetworkServerFromBungeecordServer(server)).collect(Collectors.toSet());
    }

}
