package com.lotzy.skcrew.spigot.packets.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.packets.dataWrappers.BlockPositionWrapper;
import com.lotzy.skcrew.spigot.packets.dataWrappers.ByteBufManipulator;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

@Name("Packets - Write to buffer")
@Description("Writes data to buffer")
@Examples({"on load:",
        "\tdelete file \"world_nether\""})
@Since("3.4")
public class EffWriteToBuffer extends Effect {

    static {
        Skript.registerEffect(EffWriteToBuffer.class, 
            "write bytes %bytebuf% to %bytebuf%",
            "write bool[ean] %boolean% to %bytebuf%",
            "write uuid %string% to %bytebuf%",
            "write string %string% to %bytebuf%",
            "write utf[(-| )]8 %string% to %bytebuf%",
            "write position %vector% to %bytebuf%",
            "write position %location% to %bytebuf%",
            "write [unsigned] byte %number% to %bytebuf%",
            "write [unsigned] short %number% to %bytebuf%",
            "write float %number% to %bytebuf%",
            "write double %number% to %bytebuf%",
            "write int[eger] %number% to %bytebuf%",
            "write long %number% to %bytebuf%",
            "write angle %number% to %bytebuf%",
            "write var[iable][ ]int[eger] %number% to %bytebuf%",
            "write var[iable][ ]long %number% to %bytebuf%");
            
    }
    
    private Expression<ByteBuf> buffer;
    private Expression<Vector> vector;
    private Expression<Location> location;
    private Expression<String> string;
    private Expression<ByteBuf> bufferFrom;
    private Expression<Boolean> bool;
    private Expression<Number> number;
    int pattern;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        pattern = matchedPattern;
        buffer = (Expression<ByteBuf>)expr[1];
        switch (pattern) {
            case 0:
                bufferFrom = (Expression<ByteBuf>)expr[0];
                break;
            case 1:
                bool = (Expression<Boolean>)expr[0];
                break;
            case 2:
            case 3:
            case 4:
                string = (Expression<String>)expr[0];
                break;
            case 5:
                vector = (Expression<Vector>)expr[0];
                break;
            case 6:
                location = (Expression<Location>)expr[0];
                break;
            default:
                number = (Expression<Number>)expr[0];
        }
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "write data to buffer";
    }
 
    @Override
    protected void execute(Event e) {
        ByteBuf buffer = this.buffer.getSingle(e);
        try {
            switch (pattern) {
                case 0:
                    ByteBufManipulator.writeBytes(buffer, bufferFrom.getSingle(e));
                    break;
                case 1:
                    ByteBufManipulator.writeBoolean(buffer, bool.getSingle(e));
                    break;
                case 2:
                    UUID uuid;
                    try {
                        uuid = UUID.fromString(string.getSingle(e));
                    } catch (Exception ex) { return; }
                    ByteBufManipulator.writeUUID(buffer, uuid);
                    break;
                case 3:
                    ByteBufManipulator.writeString(buffer, string.getSingle(e));
                    break;
                case 4:
                    ByteBufManipulator.writeUTF8(buffer, string.getSingle(e));
                    break;
                case 5:
                    ByteBufManipulator.writePosition(buffer, new BlockPositionWrapper(vector.getSingle(e)));
                    break;
                case 6:
                    ByteBufManipulator.writePosition(buffer, new BlockPositionWrapper(location.getSingle(e)));
                    break;
                case 7:
                    ByteBufManipulator.writeByte(buffer, number.getSingle(e).byteValue());
                    break;
                case 8:
                    ByteBufManipulator.writeShort(buffer, number.getSingle(e).shortValue());
                    break;
                case 9:
                    ByteBufManipulator.writeFloat(buffer, number.getSingle(e).floatValue());
                    break;
                case 10:
                    ByteBufManipulator.writeDouble(buffer, number.getSingle(e).doubleValue());
                    break;
                case 11:
                    ByteBufManipulator.writeInt(buffer, number.getSingle(e).intValue());
                    break;
                case 12:
                    ByteBufManipulator.writeLong(buffer, number.getSingle(e).longValue());
                    break;
                case 13:
                    ByteBufManipulator.writeAngle(buffer, number.getSingle(e).doubleValue());
                    break;
                case 14:
                    ByteBufManipulator.writeVarInt(buffer, number.getSingle(e).intValue());
                    break;
                case 15:
                    ByteBufManipulator.writeVarLong(buffer, number.getSingle(e).longValue());
                    break;
            }
        } catch (Exception ex) {
            Skript.warning(ex.getMessage());
        }
    }
}
