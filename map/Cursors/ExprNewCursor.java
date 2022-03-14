package com.lotzy.skcrew.map.Cursors;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Direction;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.map.Maps;
import com.lotzy.skcrew.map.utils.DirectionUtils;
import org.bukkit.event.Event;
import org.bukkit.map.MapCursor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprNewCursor extends SimpleExpression<MapCursor> {

    static {
        Skript.registerExpression(ExprNewCursor.class, MapCursor.class, ExpressionType.COMBINED,
                "[new] [(visible|1¦invisible)] %mapcursortype% map cursor [at [\\(][x[ ]]%-number%,[ ][y[ ]]%-number%[\\)]] [(facing|with rotation) %-direction/number%] [(named|with (name|caption)) %-string%]");
    }

    private Expression<MapCursor.Type> cursorTypeExpr;
    private Expression<Number> numberExpr1, numberExpr2;
    private Expression<?> directionExpr;
    private Expression<String> stringExpr;

    private boolean visible;

    @Override
    protected @Nullable
    MapCursor[] get(@NotNull Event e) {
        MapCursor.Type cursorType = cursorTypeExpr.getSingle(e);
        Number
                x = numberExpr1 == null ? 0 : numberExpr1.getSingle(e),
                y = numberExpr2 == null ? 0 : numberExpr2.getSingle(e);
        Object direction = directionExpr == null ? 0 : directionExpr.getSingle(e);
        String name = stringExpr == null ? null : stringExpr.getSingle(e);
        if (cursorType == null || x == null || y == null || direction == null) return null;

        byte rotation;
        if (direction instanceof Number dir)
            rotation = dir.byteValue();
        else if (direction instanceof Direction dir) {
            rotation = Maps.toCursorDirection(DirectionUtils.vectorFromDirection(dir));
        } else
            return null;
        return CollectionUtils.array(new MapCursor(x.byteValue(), y.byteValue(), rotation, cursorType, visible, name));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends MapCursor> getReturnType() {
        return MapCursor.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "new " + (visible ? "" : "in") + "visible " + cursorTypeExpr.toString(e, debug) + " map cursor" + (numberExpr1 == null ? "" : " at (" + numberExpr1.toString(e, debug) + ", " + numberExpr2.toString(e, debug) + ")") + (directionExpr == null ? "" : " with rotation " + directionExpr.toString(e, debug)) + (stringExpr == null ? "" : " named " + stringExpr.toString(e, debug));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        visible = parseResult.mark == 0;
        cursorTypeExpr = (Expression<MapCursor.Type>) exprs[0];
        numberExpr1 = (Expression<Number>) exprs[1];
        numberExpr2 = (Expression<Number>) exprs[2];
        directionExpr = exprs[3];
        stringExpr = (Expression<String>) exprs[4];
        return true;
    }
}
