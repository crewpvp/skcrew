package com.lotzy.skcrew.spigot.maps.expressions;


import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.maps.Map;
import org.bukkit.event.Event;

public class ExprDrawingMap extends SimpleExpression<Map> {
    
    static {
        Skript.registerExpression(ExprDrawingMap.class, Map.class, ExpressionType.SIMPLE, "[new|empty] drawing map");
    }

    @Override
    protected Map[] get(Event e) {
        return new Map[] {new Map()};
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
        return "new drawing map";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }
}
