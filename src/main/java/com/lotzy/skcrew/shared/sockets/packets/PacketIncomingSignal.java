package com.lotzy.skcrew.shared.sockets.packets;

import com.lotzy.skcrew.shared.sockets.data.BaseServer;
import com.lotzy.skcrew.shared.sockets.data.Signal;
import java.util.Collection;

public class PacketIncomingSignal extends Packet {
    Signal[] signals; 
    public PacketIncomingSignal(BaseServer[] receivers, Signal[] signals) {
        super(PacketType.INCOMING_SIGNAL, receivers);
        this.signals = signals;
    }
    
    public PacketIncomingSignal(BaseServer[] receivers, Collection<Signal> signals) {
        super(PacketType.INCOMING_SIGNAL, receivers);
        this.signals = signals.toArray(new Signal[0]);
    }
    
    public BaseServer[] getReceivers() {
        return (BaseServer[]) this.data;
    }
    
    public Signal[] getSignals() {
        return this.signals;
    }  
}
