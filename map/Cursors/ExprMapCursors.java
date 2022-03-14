package com.lotzy.skcrew.map.Cursors;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.map.sections.SecMapEdit;
import org.bukkit.event.Event;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursorCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ExprMapCursors extends SimpleExpression<MapCursor> {

    static {
        Skript.registerExpression(ExprMapCursors.class, MapCursor.class, ExpressionType.COMBINED,
                "[all] [[of] the] map cursors [(of|on) %-canvasmap%]");
    }

    private SecMapEdit mapEditSection;

    private Expression<MapCanvas> canvasExpr;

    @Override
    protected @Nullable
    MapCursor[] get(@NotNull Event e) {
        MapCanvas canvas = canvasExpr == null ? mapEditSection.getCanvas() : canvasExpr.getSingle(e);
        if (canvas == null) return null;
        MapCursorCollection mapCursors = canvas.getCursors();
        return IntStream.range(0, mapCursors.size())
                .mapToObj(mapCursors::getCursor)
                .toArray(MapCursor[]::new);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends MapCursor> getReturnType() {
        return MapCursor.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "all map cursors on " + canvasExpr.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        canvasExpr = (Expression<MapCanvas>) exprs[0];
        if (canvasExpr == null && mapEditSection == null) {
            Skript.error("You need to specify a map canvas when outside a map edit section");
            return false;
        }
        return true;
    }

    @Override
    public @Nullable
    Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.REMOVE_ALL ? null : CollectionUtils.array(MapCursor[].class);
    }

    @Override
    public void change(@NotNull Event e, Object @Nullable [] delta, Changer.@NotNull ChangeMode mode) {
        MapCanvas canvas = canvasExpr == null ? mapEditSection.getCanvas() : canvasExpr.getSingle(e);
        if (canvas == null) return;
        if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET)
            canvas.setCursors(new MapCursorCollection());
        else {
            if (delta == null) return;
            Stream<MapCursor> cursorStream = Arrays.stream(delta)
                    .filter(MapCursor.class::isInstance)
                    .map(MapCursor.class::cast);
            MapCursorCollection cursors;
            if (mode == Changer.ChangeMode.SET) {
                cursors = new MapCursorCollection();
                cursorStream.forEach(cursors::addCursor);
                canvas.setCursors(cursors);
            } else {
                cursors = canvas.getCursors();
                cursorStream.forEach(mode == Changer.ChangeMode.ADD ? cursors::addCursor : cursors::removeCursor);
            }
        }
    }
}


