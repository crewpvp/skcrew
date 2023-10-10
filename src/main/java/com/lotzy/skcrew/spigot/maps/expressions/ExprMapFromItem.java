package com.lotzy.skcrew.spigot.maps.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.maps.Map;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class ExprMapFromItem extends SimpleExpression<Map> {
    
    static {
        Skript.registerExpression(ExprMapFromItem.class, Map.class, ExpressionType.COMBINED, "[drawing] map (of|from) item %itemstack%");
    }
    
    private Expression<ItemStack> map;

    @Override
    protected Map[] get(Event e) {
        return new Map[] {Map.fromItem(map.getSingle(e))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Map> getReturnType() {
        return Map.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "drawing map from item";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        map = (Expression<ItemStack>) exprs[0];
        return true;
    }
}
