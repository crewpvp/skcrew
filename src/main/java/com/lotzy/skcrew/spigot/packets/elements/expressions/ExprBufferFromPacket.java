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

@Name("Packets - Get buffer from packet")
@Description("Return buffer from created packet")
@Examples({"set {_buffer} to buffer from event-packet"})
@Since("3.4")
public class ExprBufferFromPacket extends SimpleExpression<ByteBuf> {

    static {
        Skript.registerExpression(ExprBufferFromPacket.class, ByteBuf.class, ExpressionType.SIMPLE, 
                "buffer (of|from) %packet%",
                "%packet%'s buffer");
    }
    Expression<Object> packet;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        packet = (Expression<Object>) exprs[0];
        return true;
    }

    @Override
    protected ByteBuf[] get(Event event) {
        return new ByteBuf[] { PacketReflection.decodePacket(packet.getSingle(event)) };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ByteBuf> getReturnType() {
        return ByteBuf.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "buffer from packet";
    }
}