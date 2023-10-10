package com.lotzy.skcrew.proxy.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.lotzy.skcrew.proxy.sockets.SocketClientThread.SocketClientListener;

public class SocketServer implements Runnable,SocketClientListener {
    public interface ServerListener {
        default public void onClientConnection(SocketClientThread client) {};
        
        default public void onClientDisconnection(SocketClientThread client) {};
        
        default public void onReceivePacketFromClient(SocketClientThread client, Object object) {};
        
        default public void onSendPacketToClient(SocketClientThread client,Object object, boolean sended) {};
        
        default public void onInvalidPacketFromClient(SocketClientThread client) {};
        
        default public void onClientClose(SocketClientThread client) {};
        
        default public void onClose() {};
    }
    
    ServerSocket server;
    boolean enabled = true;
    
    Set<SocketClientThread> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private Set<ServerListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
    public void addListener(ServerListener toAdd) {
        listeners.add(toAdd);
    }
    
    public SocketServer(int port) throws IOException {
        this.server = new ServerSocket(port);
        this.server.setSoTimeout(0);
    }
    
    @Override
    public void run() {
        while(this.enabled) {
            try {
                Socket socket = server.accept();
                SocketClientThread client = new SocketClientThread(socket);
                client.addListener(this);
                clients.add(client);
                client.start();
            } catch (IOException ex) {}  
        }
    }
    
    public ServerSocket getServerSocket() {
        return this.server;
    }
    
    public Set<SocketClientThread> getClients() {
        return this.clients;
    }
    
    public void close() {
        this.clients.forEach(client -> client.close());
        enabled = false;
        if (server != null) try {
            server.close();
        } catch (IOException ex) {}
        this.listeners.forEach(listener -> listener.onClose());
    }
    
    @Override
    public void onDisconnection(SocketClientThread client) {
        this.clients.remove(client);
        this.listeners.forEach(listener -> listener.onClientDisconnection(client));
    }
    
    @Override
    public void onClose(SocketClientThread client) {
        this.clients.remove(client);
        this.listeners.forEach(listener -> listener.onClientClose(client));
    }
    
    @Override
    public void onConnection(SocketClientThread client) {
        this.listeners.forEach(listener -> listener.onClientConnection(client));
    }
    
    @Override
    public void onReceivePacket(SocketClientThread client, Object object) {
        this.listeners.forEach(listener -> listener.onReceivePacketFromClient(client, object));
    }
    
    @Override
    public void onSendPacket(SocketClientThread client,Object object, boolean sended) {
        this.listeners.forEach(listener -> listener.onSendPacketToClient(client, object, sended));
    }
    
    @Override
    public void onInvalidPacket(SocketClientThread client) {
        this.listeners.forEach(listener -> listener.onInvalidPacketFromClient(client));
    };
    
    public static InetAddress getExternalAddress() {
        try {
            URL url = new URL("http://checkip.amazonaws.com/");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String ip = br.readLine();
            return InetAddress.getByName(ip);
        } catch (IOException ex) {}
        return null;
    }
    
    public static List<InetAddress> getLocalV4Addresses() {
        List<InetAddress> addresses = new ArrayList<>();
        Enumeration networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ex) {
            return addresses;
        }
        while(networkInterfaces.hasMoreElements()){
            NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
            Enumeration inetAddresses = networkInterface.getInetAddresses();
            
            while(inetAddresses.hasMoreElements()) {
                InetAddress inetAddress= (InetAddress) inetAddresses.nextElement();
                if (inetAddress instanceof Inet6Address) continue;
                addresses.add(inetAddress);
            }
        }
        return addresses;
    }
    
    public static InetAddress getIpFromDomain(String domain) {
        try {
            return InetAddress.getByName(domain);
        } catch (UnknownHostException ex) {}
        return null;
    }    
    
}
