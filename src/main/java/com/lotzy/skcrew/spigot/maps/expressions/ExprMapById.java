package com.lotzy.skcrew.maps.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.maps.Map;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprMapById extends SimpleExpression<Map> {
    static {
        Skript.registerExpression(ExprMapFromItem.class, Map.class, ExpressionType.COMBINED, "[drawing] map (by|with) id %number%");
    }
    private Expression<Number> id;

    @Override
    protected Map[] get(Event e) {
        return new Map[] {Map.fromId(id.getSingle(e).intValue())};
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
    public String toString(@Nullable Event e, boolean debug) {
        return "drawing map by id";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        id = (Expression<Number>) exprs[0];
        return true;
    }
}
