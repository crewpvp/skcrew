package com.lotzy.skcrew.spigot.packets.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import static ch.njol.skript.classes.Changer.ChangeMode.ADD;
import static ch.njol.skript.classes.Changer.ChangeMode.SET;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import io.netty.buffer.ByteBuf;
import org.bukkit.event.Event;

@Name("Packets - Index of buffer")
@Description("Get or set writer/reader index in buffer")
@Examples({"set writer index of {_buffer} to 0"})
@Since("3.4")
public class ExprBufferIndex extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprBufferIndex.class, Integer.class, ExpressionType.SIMPLE, 
                "writer index of %bytebuf%",
                "%bytebuf%'s writer index",
                "reader index of %bytebuf%",
                "%bytebuf%'s reader index");
    }
    Expression<ByteBuf> buffer;
    int pattern;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        buffer = (Expression<ByteBuf>) exprs[0];
        pattern = matchedPattern;
        return true;
    }

    @Override
    protected Integer[] get(Event event) {
        int index = pattern < 2 ? buffer.getSingle(event).writerIndex() : buffer.getSingle(event).readerIndex();
        return new Integer[] {index};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<Number>[] acceptChange(Changer.ChangeMode mode) {
        switch(mode) {
            case SET:
            case ADD:
            case REMOVE:
                return CollectionUtils.array(Number.class);
            default:
                return null;
        }
    }

    @Override
    public void change(Event event,  Object[] delta, Changer.ChangeMode mode) {
        int index = ((Number)delta[0]).intValue();
        ByteBuf buf = buffer.getSingle(event);
        switch(mode) {
            case SET:
                if (pattern < 2) { buf.writerIndex(Math.min(buf.maxCapacity(), Math.max(0, index))); }
                else { buf.readerIndex(Math.min(buf.maxCapacity(), Math.max(0, index))); }
                break;
            case ADD:
                if (pattern < 2) { buf.writerIndex(Math.min(buf.maxCapacity(), Math.max(0, buf.writerIndex()+index))); }
                else { buf.readerIndex(Math.min(buf.maxCapacity(), Math.max(0, buf.readerIndex()+index))); }
                break;
            case REMOVE:
                if (pattern < 2) { buf.writerIndex(Math.min(buf.maxCapacity(), Math.max(0, buf.writerIndex()-index))); }
                else { buf.readerIndex(Math.min(buf.maxCapacity(), Math.max(0, buf.readerIndex()-index))); }
                break;
        }
    }
    
    @Override
    public Class<Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "reader / writer index";
    }
}