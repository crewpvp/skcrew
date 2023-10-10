package com.lotzy.skcrew.spigot.other;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryDragEvent;

@Name("Other - Changed slots")
@Description("Changed slots indexes in inventory drag event")
@Examples({"on inventory drag:",
        "\tbroadcast \"%event-slots%\""})
@Since("1.0")
public class ExprChangedSlots extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprChangedSlots.class, Integer.class, ExpressionType.SIMPLE,
                "changed slot[s]","event[-]slots");
    }
    
    @Override
    protected Integer[] get( Event e) {
        return ((InventoryDragEvent)e).getRawSlots().toArray(new Integer[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "get changed slots";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern,Kleenean isDelayed, SkriptParser. ParseResult parseResult) {
        if (!getParser().isCurrentEvent(InventoryDragEvent.class)) {
            Skript.error("Cannot use 'changed slots' outside of a inventory drag event", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        return true;
    }
}
