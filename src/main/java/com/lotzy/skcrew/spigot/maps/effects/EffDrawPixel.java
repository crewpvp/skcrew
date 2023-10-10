package com.lotzy.skcrew.spigot.maps.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.maps.Map;
import java.awt.Color;
import org.bukkit.event.Event;

public class EffDrawPixel extends Effect {
    
    static {
        Skript.registerEffect(EffDrawPixel.class, "draw pixel [at] "
                + "%number%(,[ ]| )%number% [with colo[u]r] "
                + "%number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%");
    }

    private Expression<Number> x;
    private Expression<Number> y;
    private Expression<Number> R;
    private Expression<Number> G;
    private Expression<Number> B;
    private Expression<Number> A;
    private Expression<Map> map;

    @Override
    protected void execute( Event e) {
        int alpha = this.A != null ? this.A.getSingle(e).intValue() : 255;
        Color color = new Color(R.getSingle(e).intValue(),G.getSingle(e).intValue(), B.getSingle(e).intValue(), alpha);
        map.getSingle(e).setPixel(x.getSingle(e).intValue(),
                y.getSingle(e).intValue(), color);
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "draw pixel";
    }

    @SuppressWarnings("all")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern,Kleenean isDelayed, SkriptParser. ParseResult parseResult) {
        x = (Expression<Number>)exprs[0];
        y = (Expression<Number>)exprs[1];
        R = (Expression<Number>)exprs[2];
        G = (Expression<Number>)exprs[3];
        B = (Expression<Number>)exprs[4];
        A = (Expression<Number>)exprs[5];
        map = (Expression<Map>)exprs[6];
        return true;
    }
}
