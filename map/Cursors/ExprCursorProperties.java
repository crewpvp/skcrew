package com.lotzy.skcrew.map.Cursors;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Direction;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.map.Maps;
import com.lotzy.skcrew.map.utils.DirectionUtils;
import org.bukkit.event.Event;
import org.bukkit.map.MapCursor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprCursorProperties extends SimplePropertyExpression<MapCursor, Object> {

    static {
        register(ExprCursorProperties.class, Object.class, "cursor (0¦type|1¦visibility|2¦x|3¦y|4¦direction|5¦rotation|6¦(name|caption))", "mapcursors");
    }

    private int pattern;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, ParseResult parseResult) {
        pattern = parseResult.mark;
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return switch (pattern) {
            case 0 -> "type";
            case 1 -> "visibility";
            case 2 -> "x";
            case 3 -> "y";
            case 4 -> "direction";
            case 5 -> "rotation";
            case 6 -> "name";
            default -> throw new IllegalStateException();
        };
    }

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public Object convert(MapCursor cursor) {
        return switch (pattern) {
            case 0 -> cursor.getType();
            case 1 -> cursor.isVisible();
            case 2 -> cursor.getX();
            case 3 -> cursor.getY();
            case 4 -> Maps.getCursorDirection(cursor);
            case 5 -> cursor.getDirection();
            case 6 -> cursor.getCaption();
            default -> null;
        };
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return switch (pattern) {
            case 0 -> MapCursor.Type.class;
            case 1 -> Boolean.class;
            case 2, 3, 5 -> Number.class;
            case 4 -> Direction.class;
            case 6 -> String.class;
            default -> throw new IllegalStateException();
        };
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return switch (mode) {
            case ADD, REMOVE -> getReturnType() == Number.class ? CollectionUtils.array(Number.class) : null;
            case RESET, DELETE -> pattern == 6 ? CollectionUtils.array(Object.class) : null;
            case SET -> CollectionUtils.array(getReturnType());
            default -> null;
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public void change(@NotNull Event e, Object @Nullable [] delta, Changer.@NotNull ChangeMode mode) {

        MapCursor[] cursors = getExpr().getArray(e);

        switch (mode) {

            case ADD -> {
                if (delta == null || !(delta[0] instanceof Number number)) return;
                for (MapCursor cursor: cursors)
                    switch (pattern) {
                        case 2 -> cursor.setX((byte) (cursor.getX() + number.intValue()));
                        case 3 -> cursor.setY((byte) (cursor.getY() + number.intValue()));
                        case 5 -> cursor.setDirection((byte) ((cursor.getDirection() + number.intValue()) & 0xF));
                    }
            }

            case REMOVE -> {
                if (delta == null || !(delta[0] instanceof Number number)) return;
                for (MapCursor cursor: cursors)
                    switch (pattern) {
                        case 2 -> cursor.setX((byte) (cursor.getX() - number.intValue()));
                        case 3 -> cursor.setY((byte) (cursor.getY() - number.intValue()));
                        case 5 -> cursor.setDirection((byte) ((cursor.getDirection() - number.intValue()) & 0xF));
                    }
            }

            case RESET, DELETE -> {
                for (MapCursor cursor: cursors)
                    cursor.caption(null);
            }

            case SET -> {
                if (delta == null || delta.length == 0) return;
                Object o = delta[0];
                for (MapCursor cursor: cursors)
                    if (pattern == 0 && o instanceof MapCursor.Type type) cursor.setType(type);
                    else if (pattern == 1 && o instanceof Boolean visible) cursor.setVisible(visible);
                    else if (pattern == 2 && o instanceof Number x) cursor.setX(x.byteValue());
                    else if (pattern == 3 && o instanceof Number y) cursor.setY(y.byteValue());
                    else if (pattern == 4 && o instanceof Direction dir) cursor.setDirection(Maps.toCursorDirection(DirectionUtils.vectorFromDirection(dir)));
                    else if (pattern == 5 && o instanceof Number rotation) cursor.setX((byte) (rotation.intValue() & 0xF));
                    else if (pattern == 6 && o instanceof String name) cursor.setCaption(name);
            }

        }

    }
}
