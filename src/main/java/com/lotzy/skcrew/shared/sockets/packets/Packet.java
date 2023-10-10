package com.lotzy.skcrew.shared.sockets.packets;

import java.io.Serializable;

public class Packet implements Serializable {
    PacketType type;
    Object data;
    
    public Packet(PacketType type, Object data) {
        this.type = type;
        this.data = data;
    }
    
    public Packet(PacketType type) {
        this.type = type;
    }
    
    public PacketType getType() {
        return this.type;
    }
    
    public Object getData() {
        return this.data;
    }
}
