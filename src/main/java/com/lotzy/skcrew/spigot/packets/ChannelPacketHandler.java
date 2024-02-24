package com.lotzy.skcrew.spigot.packets;

import com.lotzy.skcrew.spigot.packets.events.PacketEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChannelPacketHandler implements ChannelInboundHandler, ChannelOutboundHandler {
    
    final Player player;
    boolean outcomingEnabled = true;
    boolean incomingEnabled = true;
    public Channel channel;
    
    public ChannelPacketHandler(Player player, Channel channel) {
        this.player = player;
        this.channel = channel;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Channel getChannel() {
        return this.channel;
    }
    
    public boolean isOutcomingEnabled() {
        return this.outcomingEnabled;
    }
    
    public void setOutcomingEnabled(boolean enabled) {
        outcomingEnabled = enabled;
    }
    
    public boolean isIncomingEnabled() {
        return this.incomingEnabled;
    }
    
    public void setIncomingEnabled(boolean enabled) {
        incomingEnabled = enabled;
    }
    
    @Override
    public void channelRegistered(ChannelHandlerContext chc) throws Exception {
       chc.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext chc) throws Exception {
       chc.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext chc) throws Exception {
        chc.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext chc) throws Exception {
        chc.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext chc, Object o) throws Exception {
        if(outcomingEnabled) {
            PacketEvent event = new PacketEvent(player,o);
            Bukkit.getPluginManager().callEvent(event);
            if(event.isCancelled()) return;
            chc.fireChannelRead(event.getPacket());
            return;
        }
        chc.fireChannelRead(o);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext chc) throws Exception {
        chc.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext chc, Object o) throws Exception {
        chc.fireUserEventTriggered(o);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext chc) throws Exception {
       chc.fireChannelWritabilityChanged();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext chc, Throwable thrwbl) throws Exception {
        chc.fireExceptionCaught(thrwbl);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext chc) throws Exception {
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext chc) throws Exception {
    }

    @Override
    public void bind(ChannelHandlerContext chc, SocketAddress sa, ChannelPromise cp) throws Exception {
        chc.bind(sa,cp);
    }

    @Override
    public void connect(ChannelHandlerContext chc, SocketAddress sa, SocketAddress sa1, ChannelPromise cp) throws Exception {
        chc.connect(sa,sa1,cp);
    }

    @Override
    public void disconnect(ChannelHandlerContext chc, ChannelPromise cp) throws Exception {
        chc.disconnect(cp);
    }

    @Override
    public void close(ChannelHandlerContext chc, ChannelPromise cp) throws Exception {
        chc.close(cp);
    }

    @Override
    public void deregister(ChannelHandlerContext chc, ChannelPromise cp) throws Exception {
        chc.deregister(cp);
    }

    @Override
    public void read(ChannelHandlerContext chc) throws Exception {
        chc.read();
    }

    @Override
    public void write(ChannelHandlerContext chc, Object o, ChannelPromise cp) throws Exception {
        if (incomingEnabled) {
            if(o.getClass().equals(PacketReflection.ClientboundBundlePacketClass)) {
                ArrayList<Object> iterable = new ArrayList();
                for(Object packet : (Iterable)PacketReflection.BundlePacketDecoderMethod.invoke(o)) {
                    PacketEvent event = new PacketEvent(player,packet);
                    Bukkit.getPluginManager().callEvent(event);
                    if(event.isCancelled()) continue;
                    iterable.add(event.getPacket());
                }
                if(iterable.isEmpty()) return;
                chc.write(PacketReflection.BundlePacketConstructor.newInstance(iterable));
            } else {
                PacketEvent event = new PacketEvent(player,o);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) return;
                chc.write(o);
            }
            return;
        }
        chc.write(o);
    }

    @Override
    public void flush(ChannelHandlerContext chc) throws Exception {
        chc.flush();
    }
    
}
