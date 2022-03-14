package com.lotzy.skcrew.map.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.map.Maps;
import com.lotzy.skcrew.map.SkirtRenderer;
import org.bukkit.event.Event;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SecMapEdit extends EffectSection {

    static {
        Skript.registerSection(SecMapEdit.class, "[(1¦partially)] (manage|edit) [map] %map% [[and store (canvas|it|)] in %-object%]");
    }

    private Expression<MapView> mapExpr;
    private Variable<?> variable;

    private boolean replace;

    private MapCanvas canvas;

    public MapCanvas getCanvas() {
        return canvas;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
        if (exprs[0] != null) {
            if (exprs[0] instanceof Variable<?> variable)
                this.variable = variable;
            else {
                Skript.error("the map canvas can only be stored in a single variable");
                return false;
            }
        }
        replace = parseResult.mark == 0;
        mapExpr = (Expression<MapView>) exprs[1];
        if (sectionNode != null)
            loadCode(sectionNode);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event e) {

        TriggerItem next = walk(e, false);
        MapView mapView = mapExpr.getSingle(e);
        if (mapView == null) return next;

        SkirtRenderer renderer = new SkirtRenderer(mapView);
        CompletableFuture<MapCanvas> futureCanvas = renderer.getCanvas();

        if (hasSection()) {
            futureCanvas.thenAccept(canvas -> {
                Maps.LAST_CANVAS = this.canvas = canvas;
                Variables.setVariable(variable.getName().toString(e), canvas, e, variable.isLocal());
                if (first != null) walk(first, e);
            });
            return next;
        } else {
            Object locals = Variables.removeLocals(e);
            futureCanvas.thenAccept(canvas -> {
                Maps.LAST_CANVAS = this.canvas = canvas;
                Variables.setLocalVariables(e, locals);
                Variables.setVariable(variable.getName().toString(e), canvas, e, variable.isLocal());
                if (next != null) walk(next, e);
            });
            return null;
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return (replace ? "" : "partially ") + "edit " + mapExpr.toString(e, debug) + (variable == null ? "" : " and store it in " + variable.toString(e, debug));
    }

}
