package com.lotzy.skcrew.maps.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.maps.Map;
import java.awt.Color;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

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
    protected void execute(@NotNull Event e) {
        int alpha = this.A != null ? this.A.getSingle(e).intValue() : 255;
        Color color = new Color(R.getSingle(e).intValue(),G.getSingle(e).intValue(), B.getSingle(e).intValue(), alpha);
        map.getSingle(e).setPixel(x.getSingle(e).intValue(),
                y.getSingle(e).intValue(), color);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "draw pixel";
    }

    @SuppressWarnings("all")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
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
