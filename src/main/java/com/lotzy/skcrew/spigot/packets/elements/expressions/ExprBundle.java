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
import java.util.Arrays;
import org.bukkit.event.Event;

@Name("Packets - Bundle packet")
@Description("Expression to create bundle packet what contains another packets")
@Examples({"set {_bundle} to bundle packet from (create packet \"PacketPlayOutOpenSignEditor\" from {_buffer})"})
@Since("3.7")
public class ExprBundle extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprBundle.class, PacketReflection.PacketClass, ExpressionType.SIMPLE, 
                "bundle packet (from|of|with) %packets%");
    }
    
    Expression<Object> packets;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        packets = (Expression<Object>) exprs[0];
        return true;
    }

    @Override
    protected Object[] get(Event event) {
        try {
            return new Object[] { PacketReflection.BundlePacketConstructor.newInstance(Arrays.asList(packets.getAll(event))) };
        } catch (Exception ex) {
            return null;
        }
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
        return "bundle packet";
    }
}