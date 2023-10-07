package com.lotzy.skcrew.shared.sockets.data;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Utils {
    
    public static BasePlayer BasePlayerFromBungeecordPlayer(ProxiedPlayer bungeecordPlayer) {
        return new BasePlayer(bungeecordPlayer.getName(),bungeecordPlayer.getUniqueId());
    }
    
    public static BasePlayer BasePlayerFromVelocityPlayer(Player velocityPlayer) {
        return new BasePlayer(velocityPlayer.getGameProfile().getName(), velocityPlayer.getGameProfile().getId());
    }
    
    public static OfflinePlayer BukkitPlayerFromBasePlayer(BasePlayer player) {
        return Bukkit.getOfflinePlayer(player.getUUID());
    }
    
    public static NetworkPlayer NetworkPlayerFromBungeecordPlayer(ProxiedPlayer bungeecordPlayer) {
        return new NetworkPlayer(bungeecordPlayer.getName(),bungeecordPlayer.getUniqueId());
    }
    
    public static NetworkPlayer NetworkPlayerFromVelocityPlayer(Player velocityPlayer) {
        return new NetworkPlayer(velocityPlayer.getGameProfile().getName(), velocityPlayer.getGameProfile().getId());
    }
    
    public static BaseServer BaseServerFromBungeecordServer(ServerInfo bungeeCordServer) {
        return new BaseServer(bungeeCordServer.getName(),bungeeCordServer.getAddress());
    }
    
    public static BaseServer BaseServerFromVelocityServer(RegisteredServer velocityServer) {
        return new BaseServer(velocityServer.getServerInfo().getName(),velocityServer.getServerInfo().getAddress());
    }
    
    public static NetworkServer NetworkServerFromBungeecordServer(ServerInfo bungeeCordServer) {
        NetworkServer server = new NetworkServer(bungeeCordServer.getName(),bungeeCordServer.getAddress());
        bungeeCordServer.getPlayers().stream().map(player -> NetworkPlayerFromBungeecordPlayer(player)).forEach(player -> player.setServer(server));
        return server;
    }
    
    public static NetworkServer NetworkServerFromVelocityServer(RegisteredServer velocityServer) {
        NetworkServer server = new NetworkServer(velocityServer.getServerInfo().getName(),velocityServer.getServerInfo().getAddress());
        velocityServer.getPlayersConnected().stream().map(player -> NetworkPlayerFromVelocityPlayer(player)).forEach(player -> player.setServer(server));
        return server;
    }
}
