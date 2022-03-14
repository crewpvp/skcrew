package com.lotzy.skcrew.map.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.map.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprMap extends SimpleExpression<MapView> {



    static {
        Skript.registerExpression(ExprMap.class, MapView.class, ExpressionType.COMBINED,
                "map [from id] %number%",
                "[create] [new] map (from [world]|at) %world/location%",
                "all maps");
    }

    private Expression<Number> numExpr;
    private Expression<?> positionExpr;

    private int pattern;

    @Override
    protected @Nullable
    MapView[] get(@NotNull Event e) {
        return switch (pattern) {
            case 0 -> {
                Number id = numExpr.getSingle(e);
                yield  id == null ? null : CollectionUtils.array(Maps.getMaps().get(id.intValue()));
            }
            case 1 -> {
                Object position = positionExpr.getSingle(e);
                if (position instanceof World w)
                    yield CollectionUtils.array(Bukkit.createMap(w));
                else if (position instanceof Location loc) {
                    MapView map = Bukkit.createMap(loc.getWorld());
                    map.setCenterX(loc.getBlockX());
                    map.setCenterZ(loc.getBlockZ());
                    yield CollectionUtils.array(map);
                }
                yield null;
            }
            case 2 -> Maps.getMaps().values().toArray(MapView[]::new);
            default -> null;
        };
    }

    @Override
    public boolean isSingle() {
        return pattern == 2;
    }

    @Override
    public @NotNull Class<? extends MapView> getReturnType() {
        return MapView.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return switch (pattern) {
            case 0 -> "map from id " + numExpr.toString(e, debug);
            case 1 -> "map at " + positionExpr.toString(e, debug);
            case 2 -> "all maps";
            default -> throw new IllegalStateException();
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        pattern = matchedPattern;
        if (matchedPattern == 0)
            numExpr = (Expression<Number>) exprs[0];
        else if (matchedPattern == 1)
            positionExpr = exprs[0];
        return true;
    }
}
