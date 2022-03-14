package com.lotzy.skcrew.map.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.map.utils.Utils;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.event.Event;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprScaleCentreId extends SimplePropertyExpression<MapView, Long> {

    static {
        register(ExprScaleCentreId.class, Long.class, "([map] (scale|zoom)|cent(er|re) (1¦x|2¦z)|3¦map id)", "maps");
    }

    private int pattern;

    @Override
    protected @NotNull String getPropertyName() {
        return switch (pattern) {
            case 0 -> "scale";
            case 1 -> "center x";
            case 2 -> "center z";
            case 3 -> "map id";
            default -> throw new IllegalStateException();
        };
    }

    @Override
    public @Nullable Long convert(MapView mapView) {
        return (long) switch (pattern) {
            case 0 -> mapView.getScale().ordinal();
            case 1 -> mapView.getCenterX();
            case 2 -> mapView.getCenterZ();
            case 3 -> mapView.getId();
            default -> throw new IllegalStateException();
        };
    }

    @Override
    public @NotNull Class<? extends Long> getReturnType() {
        return Long.class;
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        pattern = parseResult.mark;
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable
    Class<?>[] acceptChange(@NotNull ChangeMode mode) {
        return pattern != 3 && (mode == ChangeMode.ADD || mode == ChangeMode.REMOVE || mode == ChangeMode.SET) ? CollectionUtils.array(Number.class) : null;
    }

    @Override
    public void change(@NotNull Event event, Object @Nullable [] delta, @NotNull ChangeMode mode) {
        Number old = getSingle(event);
        if (ArrayUtils.isEmpty(delta) || old == null || !(delta[0] instanceof Number change)) return;
        int value = change.intValue(), oldValue = old.intValue();
        for (MapView map : getExpr().getArray(event)) {
            int newValue = switch (mode) {
                case SET -> value;
                case ADD -> oldValue + value;
                case REMOVE -> oldValue - value;
                default -> throw new IllegalStateException();
            };
            switch (pattern) {
                case 0 -> map.setScale(Utils.enumAt(MapView.Scale.class, newValue));
                case 1 -> map.setCenterX(newValue);
                case 2 -> map.setCenterZ(newValue);
            }
        }
    }

}
