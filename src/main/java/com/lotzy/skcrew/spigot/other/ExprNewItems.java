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
import org.bukkit.inventory.ItemStack;

@Name("Other - Added items")
@Description("Added items in inventory drag event")
@Examples({"on inventory drag:",
        "\tbroadcast \"%added items%\""})
@Since("1.0")
public class ExprNewItems extends SimpleExpression<ItemStack> {

    static {
        Skript.registerExpression(ExprNewItems.class, ItemStack.class, ExpressionType.SIMPLE,
                "new items","added items");
    }
    
    @Override
    protected ItemStack[] get( Event e) {
        return ((InventoryDragEvent)e).getNewItems().values().toArray(new ItemStack[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "get added items";
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
