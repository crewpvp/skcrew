package com.lotzy.sockets;

import ch.njol.skript.Skript;
import com.lotzy.skcrew.Skcrew;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class SocketClient {
    private Socket socket            = null;
    private SocketPacket ConnectionPacket = null;
    
    private String name = null;
    public ServerInfo[] servers = null;
    
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    
    private final BukkitRunnable heartbeatThread;
    private final long heartbeatDelayMillis;
    
    public SocketClient(String address, int port, int heartbeat) {
        ConnectionPacket = SocketPacket.ConnectionPacket(Bukkit.getPort());
        heartbeatDelayMillis = heartbeat*20L;
        
        heartbeatThread = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    out.writeObject(new SocketPacket(SocketPacket.PacketType.ALIVE,null));
                } catch (IOException|NullPointerException e) {
                    try {
                        connect(address, port);
                    } catch (ClassNotFoundException|InterruptedException|IOException ex) {
                        Skript.info("Socket server offline, trying to reconnect");
                    }
                }
            }
        };
        heartbeatThread.runTaskTimerAsynchronously(Skcrew.getInstance(),0L,heartbeatDelayMillis);
        
    }
    
    public Socket getSocket() {
        return socket;
    }
    public ObjectInputStream getObjectInputStream() {
        return in;
    }
    public ObjectOutputStream getObjectOutputStream() {
        return out;
    }
    public String getName() {
        return this.name;
    }
    
    private void connect(String server, int port) throws ClassNotFoundException, InterruptedException, IOException {     
        socket = new Socket(server, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        out.writeObject(ConnectionPacket);
        out.flush();
        SocketPacket packet = (SocketPacket)in.readObject();
        Object[] body = (Object[])packet.body;
        name = (String)body[0];
        servers = (ServerInfo[])body[1];
        new ReadThread(this).runTaskAsynchronously(Skcrew.getInstance());
        Skript.info("Connected to the proxy socket "+server+":"+port);
    }
    
    public void sendPacket(SocketPacket packet) {
        try { out.writeObject(packet); out.flush(); } catch (IOException e) {}
    }
    
}
