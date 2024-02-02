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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.event.Event;

@Name("Packets - Empty buffer")
@Description("Represents buffer for fill packet")
@Examples({"set {_buffer} to empty buffer"})
@Since("3.4")
public class ExprBuffer extends SimpleExpression<ByteBuf> {

    static {
        Skript.registerExpression(ExprBuffer.class, ByteBuf.class, ExpressionType.SIMPLE, 
                "empty buffer");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    protected ByteBuf[] get(Event event) {
        return new ByteBuf[] {Unpooled.buffer()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<ByteBuf> getReturnType() {
        return ByteBuf.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "empty buffer";
    }
}