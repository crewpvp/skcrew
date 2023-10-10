package com.lotzy.skcrew.shared.sockets.packets;

import com.lotzy.skcrew.shared.sockets.data.Signal;
import java.util.Collection;

public class PacketOutcomingSignal extends Packet {
    
    public PacketOutcomingSignal(Signal[] signals) {
        super(PacketType.OUTCOMING_SIGNAL,signals);
    }
    
    public PacketOutcomingSignal(Collection<Signal> signals) {
        super(PacketType.OUTCOMING_SIGNAL, signals.toArray(new Signal[0]));
    }
    
    
    public Signal[] getSignals() {
        return (Signal[]) this.getData();
    }  
}
