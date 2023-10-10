package com.lotzy.skcrew.spigot.maps.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.maps.Map;
import java.awt.Color;
import org.bukkit.event.Event;


public class EffBezierCurve extends Effect {
    
    static {
        Skript.registerEffect(EffBezierCurve.class, "draw bezier curve (from|between) %number%(,[ ]| )%number%"
                + " [to] %number%(,[ ]| )%number% [[with] "
                + "control points] %number%(,[ ]| )%number% [and] %number%(,[ ]| )%number% "
                + "[with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%");
    }

    private Expression<Number> x0;
    private Expression<Number> y0;
    private Expression<Number> x1;
    private Expression<Number> y1;
    private Expression<Number> x2;
    private Expression<Number> y2;
    private Expression<Number> x3;
    private Expression<Number> y3;
    private Expression<Number> R;
    private Expression<Number> G;
    private Expression<Number> B;
    private Expression<Number> A;
    private Expression<Map> map;

    @Override
    protected void execute(Event e) {
         int alpha = this.A != null ? this.A.getSingle(e).intValue() : 255;
        Color color = new Color(R.getSingle(e).intValue(),G.getSingle(e).intValue(), B.getSingle(e).intValue(), alpha);
        map.getSingle(e).bezierCurve(x0.getSingle(e).intValue(),y0.getSingle(e).intValue(),
                x1.getSingle(e).intValue(),y1.getSingle(e).intValue(),
                x2.getSingle(e).intValue(),y2.getSingle(e).intValue(),
                x3.getSingle(e).intValue(),y3.getSingle(e).intValue(),color);
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "draw bezier curve";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        x0 = (Expression<Number>) exprs[0];
        y0 = (Expression<Number>) exprs[1];
        x1 = (Expression<Number>) exprs[2];
        y1 = (Expression<Number>) exprs[3];
        x2 = (Expression<Number>) exprs[4];
        y2 = (Expression<Number>) exprs[5];
        x3 = (Expression<Number>) exprs[6];
        y3 = (Expression<Number>) exprs[7];
        R = (Expression<Number>) exprs[8];
        G = (Expression<Number>) exprs[9];
        B = (Expression<Number>) exprs[10];
        A = (Expression<Number>) exprs[11];
        map = (Expression<Map>) exprs[12];
        return true;
    }
}
