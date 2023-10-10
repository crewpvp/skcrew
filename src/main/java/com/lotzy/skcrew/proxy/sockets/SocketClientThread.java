package com.lotzy.skcrew.proxy.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SocketClientThread extends Thread {
    public interface SocketClientListener {
        default public void onConnection(SocketClientThread client) {};
        
        default public void onDisconnection(SocketClientThread client) {};
        
        default public void onReceivePacket(SocketClientThread client, Object object) {};
        
        default public void onSendPacket(SocketClientThread client,Object object, boolean sended) {};
        
        default public void onInvalidPacket(SocketClientThread client) {};
        
        default public void onClose(SocketClientThread client) {};
    }
    
    Socket socket = null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    
    boolean connected = false;
    
    private Set<SocketClientListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
    public void addListener(SocketClientListener toAdd) {
        this.listeners.add(toAdd);
    }
    
    public boolean isConnected() {
        return this.connected;
    }
    
    public Socket getSocket() {
        return this.socket;
    }
    
    public SocketClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.connected = true;
    }
    
    @Override
    public void run() {
        for(SocketClientListener  listener : this.listeners)
            listener.onConnection(this);
        while(this.connected) {
            try {
                Object packet = this.in.readObject();
                this.listeners.forEach(listener -> listener.onReceivePacket(this,packet));
            } catch (ClassNotFoundException ex) {
                this.listeners.forEach(listener -> listener.onInvalidPacket(this));
            } catch (IOException ex) {
                this.disconnect();
                this.listeners.forEach(listener -> listener.onDisconnection(this));
            }
        }
    }
    
    public boolean sendObject(Object object) {   
        if (this.socket != null) {
            try {
                this.out.writeObject(object);
                this.out.flush();
                this.listeners.forEach(listener -> listener.onSendPacket(this, object, true));
                return true;
            } catch (IOException ex) {
                disconnect();
                this.listeners.forEach(listener -> listener.onDisconnection(this));
            } 
        }
        this.listeners.forEach(listener -> listener.onSendPacket(this, object, false));
        return false;
    }
    
    private void disconnect() {
        if (!this.connected) return;
        this.connected = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException ex) {}
    }
    
    public void close() {
        this.disconnect();
        this.listeners.forEach(listener -> listener.onClose(this));  
    }
    
    @Override
    public boolean equals(Object object) {
        return object instanceof SocketClientThread && this.getSocket().equals(((SocketClientThread)object).getSocket());
    }

    @Override
    public int hashCode() {
        return 228 + Objects.hashCode(this.socket);
    }
}
