package com.lotzy.skcrew.map.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprMapStates extends SimplePropertyExpression<MapView, Boolean> {

    static {
        register(ExprMapStates.class, Boolean.class,
                "[map] (0¦lock[ed]|1¦position tracking|2¦unlimited tracking) [stat(e|us)] ", "maps");
    }

    private int pattern;

    @Override
    protected @NotNull String getPropertyName() {
        return switch (pattern) {
            case 0 -> "lock";
            case 1 -> "position tracking";
            case 2 -> "unlimited tracking";
            default -> throw new IllegalStateException();
        } + " state";
    }

    @Override
    public @Nullable Boolean convert(MapView mapView) {
        return switch (pattern) {
            case 0 -> mapView.isLocked();
            case 1 -> mapView.isTrackingPosition();
            case 2 -> mapView.isUnlimitedTracking();
            default -> null;
        };
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        pattern = parseResult.mark;
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }


    @Override
    public @Nullable
    Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? CollectionUtils.array(Boolean.class) : null;
    }

    @Override
    public void change(@NotNull Event e, Object @Nullable [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta != null && delta.length != 0 && delta[0] instanceof Boolean state)
            for (MapView map: getExpr().getArray(e))
                switch (pattern) {
                    case 0 -> map.setLocked(state);
                    case 1 -> map.setTrackingPosition(state);
                    case 2 -> map.setUnlimitedTracking(state);
                }
    }
}