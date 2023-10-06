package com.lotzy.skcrew.maps.expressions;


import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.maps.Map;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class ExprItemFromMap extends SimpleExpression<ItemStack> {
    static {
        Skript.registerExpression(ExprItemFromMap.class, ItemStack.class, ExpressionType.COMBINED, "[the] map item (from|of) %map%");
    }
    private Expression<Map> map;

    @Override
    protected @Nullable ItemStack[] get(Event e) {
        return new ItemStack[] {map.getSingle(e).buildItem()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "map item";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        map = (Expression<Map>) exprs[0];
        return true;
    }
}
