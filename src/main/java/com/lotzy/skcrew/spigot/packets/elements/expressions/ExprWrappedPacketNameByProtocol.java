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
import com.lotzy.skcrew.spigot.packets.packetWrappers.AbstractPacket;
import java.util.Map.Entry;
import org.bukkit.event.Event;

@Name("Packets - Get wrappred packet name by protocols")
@Description("Get packet name by id, bound, state from protocols wiki")
@Examples({"broadcast packet name of id 1, state \"play\", bound \"clientbound\""})
@Since("3.4")
public class ExprWrappedPacketNameByProtocol extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprWrappedPacketNameByProtocol.class, String.class, ExpressionType.SIMPLE, 
                "[wrapped] packet name (by|of) id %number%[( and|([ ],))][ ]state %string%[( and|([ ],))][ ]bound %string%");
    }
    
    Expression<Number> id;
    Expression<String> state;
    Expression<String> bound;
    
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        id = (Expression<Number>) exprs[0];
        state = (Expression<String>) exprs[1];
        bound = (Expression<String>) exprs[2];
        return true;
    }

    @Override
    protected String[] get(Event event) {
        int id = this.id.getSingle(event).intValue();
        String state = parseState(this.state.getSingle(event));
        String bound = parseBound(this.bound.getSingle(event));
        Entry<String,AbstractPacket> packet = PacketReflection.PACKETS.entrySet().stream().filter(entry -> 
                    entry.getValue().getID() == id &&
                    entry.getValue().getState().equals(state) &&
                    entry.getValue().getBound().equals(bound)).findFirst().orElse(null);
        if (packet != null) return new String[] { packet.getKey() };
        return null;
    }
    
    private String parseState(String state) {
        return PacketReflection.States.stream().filter(astate -> astate.startsWith(state.toLowerCase())).findFirst().orElse("");
    }
    
    private String parseBound(String bound) {
        return PacketReflection.Bounds.stream().filter(abound -> abound.startsWith(bound.toLowerCase())).findFirst().orElse("");
    }
    
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "get wrapped packet name by protocols";
    }
}