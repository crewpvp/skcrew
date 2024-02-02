package com.lotzy.skcrew.spigot.packets.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.packets.PacketReflection;
import io.netty.buffer.ByteBuf;
import org.bukkit.event.Event;

@Name("Packets - Create packet")
@Description("Trying return packet if constructor is found")
@Examples({"set {_buffer} to packet \"PacketPlayOutOpenSignEditor\" of {_buffer}"})
@Since("3.4")
public class ExprCreatePacket extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprCreatePacket.class, PacketReflection.PacketClass, ExpressionType.SIMPLE, 
                "[create] packet %string% (from|of|with) %bytebuf%");
    }
    
    Expression<ByteBuf> buffer;
    Expression<String> packetName;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        packetName = (Expression<String>) exprs[0];
        buffer = (Expression<ByteBuf>) exprs[1];
        return true;
    }

    @Override
    protected Object[] get(Event event) {
        return new Object[] {PacketReflection.createPacket(packetName.getSingle(event),buffer.getSingle(event))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<Object> getReturnType() {
        return PacketReflection.PacketClass;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "create packet";
    }
}