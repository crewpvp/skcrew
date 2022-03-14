package com.lotzy.skcrew.map.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.map.Maps;
import com.lotzy.skcrew.map.sections.SecMapEdit;
import org.bukkit.event.Event;
import org.bukkit.map.MapCanvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprLastEdited extends SimpleExpression<MapCanvas> {

    static {
        Skript.registerExpression(ExprLastEdited.class, MapCanvas.class, ExpressionType.SIMPLE,
                "[the] [last] [edited] [map] canvas");
    }

    private SecMapEdit mapEditSection;


    @Override
    @Nullable
    protected MapCanvas[] get(@NotNull Event event) {
        MapCanvas canvas = mapEditSection == null ? Maps.LAST_CANVAS : mapEditSection.getCanvas();
        return canvas == null ? null : CollectionUtils.array(canvas);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends MapCanvas> getReturnType() {
        return MapCanvas.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "last map canvas";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        return true;
    }

}
