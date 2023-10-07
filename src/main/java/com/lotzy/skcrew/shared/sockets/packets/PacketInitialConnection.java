package com.lotzy.skcrew.shared.sockets.packets;

public class PacketInitialConnection extends Packet {
    
    public PacketInitialConnection(int port) {
        super(PacketType.INITIAL_CONNECTION, port);
    }
    
    public int getPort() {
        return (int) this.data;
    }
    
}
