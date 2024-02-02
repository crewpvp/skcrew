package com.lotzy.skcrew.spigot.packets.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Packets - Packet handle of player")
@Description("Enable or disable packet handling for player")
@Examples({"set listen incoming packets of player to false"})
@Since("3.4")
public class ExprPacketHandleOfPlayer extends SimpleExpression<Boolean> {

    static {
        Skript.registerExpression(ExprPacketHandleOfPlayer.class, Boolean.class, ExpressionType.SIMPLE, 
                "[(handl(e|ing))|(listen[ing] [of])] incoming packets of %player%",
                "%player%'s [(handl(e|ing))|(listen[ing] [of])] incoming packets",
                "[(handl(e|ing))|(listen[ing] [of])] outcoming packets of %player%",
                "%player%'s [(handl(e|ing))|(listen[ing] [of])] outcoming packets");
    }

    Expression<Player> player;
    boolean incoming;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        incoming = matchedPattern < 2;
        return true;
    }

    @Override
    protected Boolean[] get(Event event) {
        if (incoming) 
            return new Boolean[] {PacketReflection.getInjectedHandlerFromPlayer(player.getSingle(event)).isIncomingEnabled()};
        return new Boolean[] {PacketReflection.getInjectedHandlerFromPlayer(player.getSingle(event)).isOutcomingEnabled()};
    }

    @Override

    public Class<Boolean>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            return new Class[] { Boolean.class };
        }
        return null;
    }

    @Override
    public void change(Event event,  Object[] delta, ChangeMode mode) {
        if (incoming) {
            PacketReflection.getInjectedHandlerFromPlayer(player.getSingle(event)).setIncomingEnabled((boolean)delta[0]);
            return;
        }
        PacketReflection.getInjectedHandlerFromPlayer(player.getSingle(event)).setOutcomingEnabled((boolean)delta[0]);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "listen packets";
    }
}