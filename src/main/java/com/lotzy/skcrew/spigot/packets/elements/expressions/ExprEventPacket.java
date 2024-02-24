package com.lotzy.skcrew.spigot.packets.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.packets.PacketReflection;
import com.lotzy.skcrew.spigot.packets.elements.events.EvtPacket;
import com.lotzy.skcrew.spigot.packets.events.PacketEvent;
import org.bukkit.event.Event;

@Name("Packets - Event packet")
@Description("Return handled packet in packet event")
@Examples({"on packet:",
            "\tbroadcast \"%event-packet%\""
})
@Since("3.4")
public class ExprEventPacket extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprEventPacket.class, PacketReflection.PacketClass, ExpressionType.SIMPLE, "[event-]packet" );
    }

    private boolean isDelayed;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
        if (!(skriptEvent instanceof EvtPacket)) {
            Skript.error("You can't use 'event-packet' outside of a Packet event.");
            return false;
        }

        return true;
    }

    @Override
    protected Object[] get(Event event) {
        return new Object[] {((PacketEvent) event).getPacket()};
    }

    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (isDelayed) {
            Skript.error("You can't set the 'event-packet' when the event is already passed.");
            return null;
        }

        if (mode == ChangeMode.SET) {
            return new Class[]{ PacketReflection.PacketClass };
        }

        return null;
    }

    @Override
    public void change(Event event,  Object[] delta, ChangeMode mode) {
        if (delta == null || !(event instanceof PacketEvent)) {
            return;
        }
        ((PacketEvent) event).setPacket(delta[0]);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<?> getReturnType() {
        return PacketReflection.PacketClass;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "event-packet";
    }
}