package com.lotzy.skcrew.spigot.maps.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.maps.Map;
import java.awt.Color;
import org.bukkit.event.Event;


public class EffDrawCircle extends Effect {

    static {
        Skript.registerEffect(EffDrawCircle.class, "draw circle at %number%(,[ ]| )%number% "
                + "with radius %number% [with colo[u]r] "
                + "%number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%");
    }

    private Expression<Number> x;
    private Expression<Number> y;
    private Expression<Number> radius;
    private Expression<Number> R;
    private Expression<Number> G;
    private Expression<Number> B;
    private Expression<Number> A;
    private Expression<Map> map;

    @Override
    protected void execute(Event e) {
        int alpha = this.A != null ? this.A.getSingle(e).intValue() : 255;
        Color color = new Color(R.getSingle(e).intValue(),G.getSingle(e).intValue(), B.getSingle(e).intValue(), alpha);
        map.getSingle(e).drawCircle(x.getSingle(e).intValue(), y.getSingle(e).intValue(),
                radius.getSingle(e).intValue(), color);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "draw circle";
    }

    @SuppressWarnings("all")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        x = (Expression<Number>) exprs[0];
        y = (Expression<Number>) exprs[1];
        radius = (Expression<Number>) exprs[2];
        R = (Expression<Number>) exprs[3];
        G = (Expression<Number>) exprs[4];
        B = (Expression<Number>) exprs[5];
        A = (Expression<Number>) exprs[6];
        map = (Expression<Map>) exprs[7];
        return true;
    }
}
