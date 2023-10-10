package com.lotzy.skcrew.spigot.maps.expressions;


import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.maps.Map;
import java.awt.Color;
import org.bukkit.event.Event;

public class ExprPixelOfMap extends SimpleExpression<Number> {
    
    static {
        Skript.registerExpression(ExprPixelOfMap.class, Number.class, ExpressionType.SIMPLE, "pixel %number%(,[ ]| )%number% of %map%");
    }
    
    Expression<Number> x;
    Expression<Number> y;
    Expression<Map> map;
    
    @Override
    protected Number[] get(Event e) {
        Color color = map.getSingle(e).getPixel(x.getSingle(e).intValue(), y.getSingle(e).intValue());
        return new Number[] {color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha()};
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "pixel of map";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        x = (Expression<Number>)exprs[0];
        y = (Expression<Number>)exprs[0];
        map = (Expression<Map>)exprs[0];
        return true;
    }
}
