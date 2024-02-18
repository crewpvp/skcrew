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
import com.lotzy.skcrew.spigot.packets.dataWrappers.ByteBufManipulator;
import io.netty.buffer.ByteBuf;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

@Name("Packets - Read from buffer")
@Description("Allow read data from buffer")
@Examples({"set {_buffer} to read byte from {_buffer}"})
@Since("3.4")
public class ExprReadFromBuffer extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprReadFromBuffer.class, Object.class, ExpressionType.SIMPLE, 
            "read bool[ean] from %bytebuf%",
            "read uuid from %bytebuf%",
            "read string from %bytebuf%",
            "read position from %bytebuf%",
            "read [unsigned] byte from %bytebuf%",
            "read [unsigned] short from %bytebuf%",
            "read float from %bytebuf%",
            "read double from %bytebuf%",
            "read int[eger] from %bytebuf%",
            "read long from %bytebuf%",
            "read angle from %bytebuf%",
            "read var[iable][ ]int[eger] from %bytebuf%",
            "read var[iable][ ]long from %bytebuf%",
            "read utf[(-| )]8 [with [len[gth]]] %number% from %bytebuf%");
    }
    Expression<ByteBuf> buffer;
    int pattern;
    Expression<Number> len;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pattern = matchedPattern;
        
        if(pattern == 13) {
            len = (Expression<Number>)exprs[0];
            buffer = (Expression<ByteBuf>)exprs[1];
        } else {
            buffer = (Expression<ByteBuf>)exprs[0];
        }
        return true;
    }

    @Override
    protected Object[] get(Event event) {
        try {
            ByteBuf buffer = this.buffer.getSingle(event);
            switch (pattern) {
                case 0:
                    return new Boolean[]{ByteBufManipulator.readBoolean(buffer)};
                case 1:
                    return new String[]{ByteBufManipulator.readUUID(buffer).toString()};
                case 2:
                    return new String[]{ByteBufManipulator.readString(buffer)};
                case 3:
                    return new Vector[]{ByteBufManipulator.readPosition(buffer).asVector()};
                case 4:
                    return new Byte[]{ByteBufManipulator.readByte(buffer)};
                case 5:
                    return new Short[]{ByteBufManipulator.readShort(buffer)};
                case 6:
                    return new Float[]{ByteBufManipulator.readFloat(buffer)};
                case 7:
                    return new Double[]{ByteBufManipulator.readDouble(buffer)};
                case 8:
                    return new Integer[]{ByteBufManipulator.readInt(buffer)};
                case 9:
                    return new Long[]{ByteBufManipulator.readLong(buffer)};
                case 10:
                    return new Double[]{ByteBufManipulator.readAngle(buffer)};
                case 11:
                    return new Integer[]{ByteBufManipulator.readVarInt(buffer)};
                case 12:
                    return new Long[]{ByteBufManipulator.readVarLong(buffer)};
                default:
                    return new String[]{ByteBufManipulator.readUTF8(buffer, len.getSingle(event).intValue())};
            }
        } catch (Exception ex) {
            Skript.warning(ex.getMessage());
            return null;
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<?> getReturnType() {
        switch (pattern) {
            case 0:
                return Boolean.class;
            case 1:
            case 2:
                return String.class;
            case 3:
                return Vector.class;
            case 4:
                return Byte.class;
            case 5:
                return Short.class;
            case 6:
                return Float.class;
            case 7:
                return Double.class;
            case 8:
                return Integer.class;
            case 9:
                return Long.class;
            case 10:
                return Double.class;
            case 11:
                return Integer.class;
            case 12:
                return Long.class;
            default:
                return String.class;
        }
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "read from buffer";
    }
}