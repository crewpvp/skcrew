package com.lotzy.skcrew.shared.sockets.data;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Objects;

public class BaseServer implements Serializable {
    
    private final InetSocketAddress address;
    private final String name;
    private boolean connected = false;
    
    public BaseServer(String name, InetSocketAddress address) {
        this.name = name;
        this.address = address;
    }
    
    public BaseServer(String name, InetSocketAddress address, boolean connected) {
        this.name = name;
        this.address = address;
        this.connected = connected;
    }
    
    public boolean isConnected() {
        return this.connected;
    }
    
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    public void setDisconnected() {
        this.connected = false;
    }
    
    public String getName() {
        return this.name;
    }
    
    public InetSocketAddress getInetSocketAddress() {
        return this.address;
    }
    
    public String getIP() {
        InetAddress address = this.address.getAddress();
        if (address == null) return this.getHostName().toLowerCase().equals("localhost") ? "127.0.0.1" : this.getHostName();
        return address.toString().split("/")[1];
    }
    
    public int getPort() {
        return this.address.getPort();
    }
    
    public String getHostName() {
        return this.address.getHostString();
    }
    
    @Override
    public boolean equals(Object object) {
        return (object instanceof BaseServer)  && ((BaseServer)object).getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        return 1488*Objects.hashCode(this.getName());
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
