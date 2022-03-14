package com.lotzy.skcrew.map.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.map.sections.SecMapEdit;
import com.lotzy.skcrew.map.SkirtRenderer;
import org.bukkit.event.Event;
import org.bukkit.map.MapCanvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffWaitRender extends AsyncEffect {

    static {
        Skript.registerEffect(EffWaitRender.class,
                "wait for [next] render",
                "wait for %canvasmap% to render");
    }

    private Expression<MapCanvas> canvasExpr;

    private SecMapEdit mapEditSection;

    @Override
    protected void execute(@NotNull Event e) {
        MapCanvas canvas = canvasExpr == null ? mapEditSection.getCanvas() : canvasExpr.getSingle(e);
        if (canvas != null) SkirtRenderer.getRenderer(canvas).getCanvas().join();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "wait for " + (canvasExpr == null ? "next render" : canvasExpr.toString(e, debug) + " to render");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            if (mapEditSection == null) {
                Skript.error("You need to specify a map canvas when outside a map edit section");
                return false;
            }
            return true;
        }
        canvasExpr = (Expression<MapCanvas>) exprs[0];
        return true;
    }
}
