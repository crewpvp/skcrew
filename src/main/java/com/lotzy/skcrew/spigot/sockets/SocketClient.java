package com.lotzy.skcrew.spigot.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class SocketClient implements Runnable {
    public interface ClientListener {
        default public void onConnection(InetSocketAddress address) {};
        
        default public void onDisconnection() {};
        
        default public void onReceivePacket(Object object) {};
        
        default public void onSendPacket(Object object, boolean sended) {};
        
        default public void onInvalidPacket() {};
        
        default public void onReconnection() {};
        
        default public void onClose() {};
    }
    
    String host;
    int port;
    
    Socket socket = null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    
    boolean enabled = true;
    boolean connected = false;
    
    int reconnectionTime;
    
    private Set<ClientListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
    public SocketClient(String host, int port, int reconnectionTime) {
        this.host = host;
        this.port = port;
        this.reconnectionTime = reconnectionTime;
    }
    
    public void addListener(ClientListener toAdd) {
        this.listeners.add(toAdd);
    }
    
    public boolean isConnected() {
        return this.connected;
    }
    
    public boolean isClosed() {
        return !this.enabled;
    }
    
    public int getReconnectionTime() {
        return this.reconnectionTime;
    }
    
    public Socket getSocket() {
        return this.socket;
    }
    
    @Override
    public void run() {
        try {
            this.connect();
        } catch (IOException ex) {
            this.reconnect();
        }
        while(this.enabled) {
            try {
                Object packet = this.in.readObject();
                for(ClientListener listener : this.listeners)
                    listener.onReceivePacket(packet);
            } catch (ClassNotFoundException ex) {
                for(ClientListener listener : this.listeners)
                    listener.onInvalidPacket();
            } catch (IOException ex) {
                if (this.enabled) {
                    for(ClientListener listener : this.listeners)
                        listener.onDisconnection();
                    this.reconnect();
                }
            }
        }
    }
    
    public void connect() throws IOException {
        this.socket = new Socket(this.host, this.port);
        this.socket.setSoTimeout(0);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.connected = true;
        for(ClientListener listener : this.listeners)
            listener.onConnection((InetSocketAddress)this.socket.getRemoteSocketAddress());
    }
    
    public void reconnect() {
        this.connected = false;
        try {
            if (this.out != null) this.out.close();
            if (this.in != null) this.in.close();
            if (this.socket != null) this.socket.close();
            this.out = null;
            this.in = null;
            this.socket = null;
        } catch (IOException ex) {}
        
        while (this.enabled) {
            for(ClientListener listener : this.listeners)
                listener.onReconnection();
            try {
                Thread.sleep(this.reconnectionTime);
            } catch (InterruptedException ex) {}
            try {
                this.connect();
                return;
            } catch (IOException ex) {}
        }
    }
    
    public boolean sendObject(Object object) {   
        if (this.socket != null && this.out != null) {
            try {
                this.out.writeObject(object);
                this.out.flush();
                for(ClientListener listener : this.listeners)
                    listener.onSendPacket(object, true);
                return true;
            } catch (IOException ex) {} 
        }
        for(ClientListener listener : this.listeners)
            listener.onSendPacket(object, false);
        return false;
    }
    
    public void close() {
        if (!this.enabled) return;
        this.enabled = false;
        try {
            if (out != null) this.out.close();
            if (in != null) this.in.close();
            if (socket != null) socket.close();
        } catch (IOException ex) {}
        this.connected = false;
        for(ClientListener listener : this.listeners)
            listener.onClose();
    }
}
