package com.lotzy.skcrew.spigot.packets.packetWrappers;

import ch.njol.skript.Skript;
import com.lotzy.skcrew.spigot.packets.PacketReflection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Method;
import java.util.Objects;

public class BasePacket implements AbstractPacket {
    
    Class packet;
    int id;
    String state;
    String bound;
    Method decoder = null;
    
    public BasePacket(Class packet, int id, String state, String bound) {
        this.packet = packet;
        this.id = id;
        this.state = state;
        this.bound = bound;
    }
    

    @Override
    public Object createPacket(ByteBuf buffer) {
        return null;
    }

    public Class getPacket() {
        return this.packet;
    }
    
    public String getBound() {
        return this.bound;
    }
    
    public String getState() {
        return this.state;
    }
    
    public int getID() {
        return this.id;
    }
    
    public void setDecoder(Method decoder) {
        if(this.decoder==null) this.decoder = decoder;
    }
    
    public Method getDecoder() {
        return this.decoder;
    }
    
    @Override
    public int hashCode() {
        return 1337*Objects.hashCode(this.packet);
    }
    
    public ByteBuf decodePacket(Object packet) {
        try {
            Object buffer = PacketReflection.createFriendlyBuffer.apply(Unpooled.buffer());
            this.decoder.invoke(packet,buffer);
            return (ByteBuf)PacketReflection.PacketDataSerializerByteBufField.get(buffer);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public void creationError() {
        Skript.warning("Something wrong with buffer when you create packet '" + packet.getSimpleName() + "'." + System.lineSeparator() +
        "Go to https://wiki.vg/Protocol_version_numbers#Release and read fields of packet: " + System.lineSeparator() +
        "id '0x" + Integer.toHexString(id) + "', state '"+state+"' and bound '"+bound+"'");
    }
    
}
