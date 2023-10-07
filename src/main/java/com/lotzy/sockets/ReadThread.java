package com.lotzy.sockets;

import ch.njol.skript.Skript;
import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.skcrew.spigot.sockets.SignalEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ReadThread extends BukkitRunnable {
    private final ObjectInputStream in;
    private final SocketClient client;
    
    public ReadThread(SocketClient client) {
        this.client = client;
        in = client.getObjectInputStream();
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                SocketPacket packet = (SocketPacket) in.readObject();
                switch(packet.type) {
                    case PLAYER_LEAVE:
                        onPlayerLeave(packet);
                        break;
                    case PLAYER_JOIN:
                        onPlayerJoin(packet);
                        break;
                    case SIGNAL:
                        onSignal(packet);
                        break;
                    case COMMAND:
                        onCommand(packet);
                        break;
                    case SERVER_CONNECTED:
                        onServerConnect(packet);
                        break;
                    case SERVER_DISCONNECTED:
                        onServerDisconnect(packet);
                        break;
                }
            } catch (IOException|ClassNotFoundException|NullPointerException ex) { break; }
        }
    }
    public void onServerDisconnect(SocketPacket packet) {
        String server = (String)packet.body;
        for (ServerInfo srv : client.servers)
            if (srv.getName().equals(server))
                srv.setOffline();
    }
    public void onServerConnect(SocketPacket packet) {
        String server = (String)packet.body;
        for (ServerInfo srv : client.servers)
            if (srv.getName().equals(server))
                srv.setOnline();
    }
    
    public void onCommand(SocketPacket packet) {
        Bukkit.getScheduler().runTask(Skcrew.getInstance(), new Runnable() {
            @Override
            public void run() {  
                for(String cmd : (String[])packet.body)
                    Skript.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        });
    }
    
    public void onSignal(SocketPacket packet) {
        Object[] body = (Object[])packet.body;
        Bukkit.getScheduler().runTask(Skcrew.getInstance(), new Runnable() {
            @Override
            public void run() { 
                Bukkit.getServer().getPluginManager().callEvent(new SignalEvent((String)body[0],body[1])); 
            }
        });
    }
    
    public void onPlayerJoin(SocketPacket packet) {
        String[] body = (String[])packet.body;
        for (ServerInfo srv : client.servers)
            if (srv.getName().equals(body[1]))
                srv.addPlayer((String)body[0]);
    }
    public void onPlayerLeave(SocketPacket packet) {
        String[] body = (String[])packet.body;
        for (ServerInfo srv : client.servers)
            if (srv.getName().equals(body[1]))
                srv.removePlayer((String)body[0]);
    }
}
