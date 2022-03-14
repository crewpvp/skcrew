package com.lotzy.skcrew.other;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprChangedSlots extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprChangedSlots.class, Integer.class, ExpressionType.SIMPLE,
                "changed slot[s]","event[-]slots");
    }
    @Override
    protected Integer[] get(@NotNull Event e) {
        return ((InventoryDragEvent)e).getRawSlots().toArray(new Integer[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "get changed slots";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(InventoryDragEvent.class)) {
            Skript.error("Cannot use 'changed slots' outside of a inventory drag event", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        return true;
    }
}
