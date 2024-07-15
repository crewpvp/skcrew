package com.lotzy.skcrew.velocity;

import com.google.inject.Inject;
import com.lotzy.skcrew.proxy.Skcrew;
import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.shared.sockets.data.BaseServer;
import com.lotzy.skcrew.shared.sockets.data.Utils;
import com.lotzy.skcrew.proxy.SkcrewImpl;
import com.lotzy.skcrew.shared.sockets.data.NetworkServer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent.LoginStatus;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.slf4j.Logger;

@Plugin(id="skcrew", name="Skcrew", url = "https://skcrew.crewpvp.xyz", version = "3.8", description = "Socket based data transfer for Skript addon Skcrew", authors = {"Lotzy"})
public class Velocity implements SkcrewImpl {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    
    private Skcrew skcrew;
    
    @Inject
    public Velocity(ProxyServer server, Logger logger,@DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.skcrew = new Skcrew(this);
    }

    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public Path getDataDirectory() {
        return this.dataDirectory;
    }
    
    public ProxyServer getProxyServer() {
        return server;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.server.getScheduler().buildTask(this, this.skcrew.getSocketServer()).schedule();
        this.server.getScheduler().buildTask(this, this.skcrew.getWebServer()).schedule();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (this.skcrew.getWebServer() != null) this.skcrew.getWebServer().close();
        if (this.skcrew.getSocketServer() != null) this.skcrew.getSocketServer().close();
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        if (this.skcrew.getSocketServerListener() == null) return;
        BasePlayer player = Utils.BasePlayerFromVelocityPlayer(event.getPlayer());
        BaseServer newserver = Utils.BaseServerFromVelocityServer(event.getServer());
        if (!event.getPreviousServer().isPresent()) {
            this.skcrew.getSocketServerListener().onPlayerConnectedProxy(player);
            this.skcrew.getSocketServerListener().onPlayerConnectedServer(player, newserver);
        } else {
            BaseServer server = Utils.BaseServerFromVelocityServer(event.getPreviousServer().get());
            this.skcrew.getSocketServerListener().onPlayerSwitchServer(player, server, newserver);
        } 
    }
    
    @Subscribe
    public void onKickedFromServer(KickedFromServerEvent event) {
        if (this.skcrew.getSocketServerListener() == null) return;
        BasePlayer player = Utils.BasePlayerFromVelocityPlayer(event.getPlayer());
        BaseServer server = Utils.BaseServerFromVelocityServer(event.getServer());
        this.skcrew.getSocketServerListener().onPlayerDisconnectedServer(player, server);
    }
    
    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        if (this.skcrew.getSocketServerListener() == null) return;
        if (event.getLoginStatus() != LoginStatus.SUCCESSFUL_LOGIN) return;
        BasePlayer player = Utils.BasePlayerFromVelocityPlayer(event.getPlayer());
        if (event.getPlayer().getCurrentServer().isPresent()) {
            BaseServer server = Utils.BaseServerFromVelocityServer(event.getPlayer().getCurrentServer().get().getServer());
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
        Optional<Player> optionalPlayer = this.getProxyServer().getPlayer(uuid);
        if (!optionalPlayer.isPresent()) return null;
        Player player = optionalPlayer.get();
        return Utils.NetworkPlayerFromVelocityPlayer(player);
    }
    
    @Override
    public BasePlayer getPlayer(String name) {
        Optional<Player> optionalPlayer = this.getProxyServer().getPlayer(name);
        if (!optionalPlayer.isPresent()) return null;
        Player player = optionalPlayer.get();
        return Utils.NetworkPlayerFromVelocityPlayer(player);
    }
    
    @Override
    public BaseServer getServer(String name) {
        Optional<RegisteredServer> optionalServer = this.getProxyServer().getServer(name);
        if (!optionalServer.isPresent()) return null;
        RegisteredServer server = optionalServer.get();
        return Utils.NetworkServerFromVelocityServer(server);
    }
    
    @Override
    public void switchPlayers(BasePlayer[] players, BaseServer server) {
        Optional<RegisteredServer> optionalServer = this.getProxyServer().getServer(server.getName());
        if (!optionalServer.isPresent()) return;
        RegisteredServer registeredServer = optionalServer.get();
        for (BasePlayer basePlayer : players) {
            Optional<Player> optionalPlayer = this.getProxyServer().getPlayer(basePlayer.getUUID());
            if (!optionalPlayer.isPresent()) continue;
            Player player = optionalPlayer.get();
            player.createConnectionRequest(registeredServer).fireAndForget();
        }   
    }
    
    @Override
    public void kickPlayers(BasePlayer[] players, String reason) {
        Component component = reason != null ? JSONComponentSerializer.json().deserialize(reason) : Component.text("");
        for (BasePlayer p : players) {
            Optional<Player> c = this.getProxyServer().getPlayer(p.getUUID());
            if (c.isPresent()) c.get().disconnect(component);
        }
    }
    
    @Override
    public Set<NetworkServer> getAllServers() {
        return this.getProxyServer().getAllServers().stream().map(server -> Utils.NetworkServerFromVelocityServer(server)).collect(Collectors.toSet());
    }
}
