package com.lotzy.skcrew.map.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.map.Maps;
import com.lotzy.skcrew.map.sections.SecMapEdit;
import org.bukkit.event.Event;
import org.bukkit.map.MapCanvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;

public class ExprMapPixel extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprMapPixel.class, Number.class, ExpressionType.COMBINED,
                "pixel at " + Maps.coordPattern(false) + " [(of|on) %-canvasmap%]",
                "pixels (between|2¦within) " + Maps.coordPattern(false) + " and " + Maps.coordPattern(false) + " [(of|on) %-canvasmap%]",
                "all pixels [(of|on) %-canvasmap%]");
    }

    private SecMapEdit mapEditSection;

    private Expression<Number> numberExpr1, numberExpr2, numberExpr3, numberExpr4;
    private Expression<MapCanvas> canvasExpr;

    private int pattern;

    @Override
    protected @Nullable
    Number[] get(@NotNull Event e) {
        MapCanvas canvas = canvasExpr == null ? mapEditSection.getCanvas() : canvasExpr.getSingle(e);
        if (canvas == null || pattern != 1) return null;
        Number x = numberExpr1.getSingle(e), y = numberExpr2.getSingle(e);
        return (x == null || y == null) ? null : CollectionUtils.array(canvas.getPixel(x.intValue() - 1, y.intValue() - 1));
    }

    @Override
    public boolean isSingle() {
        return pattern == 0;
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return switch (pattern) {
            case 0 -> "pixel at (" + numberExpr1.toString(e, debug) + ", " + numberExpr2.toString(e, debug) + ") on " + canvasExpr.toString(e, debug);
            case 1, 3 -> "pixels " + (pattern == 1 ? "between" : "within") + " (" + numberExpr1.toString(e, debug) + ", " + numberExpr2.toString(e, debug) + ") and (" + numberExpr3.toString(e, debug) + ", " + numberExpr4.toString(e, debug) + ") on " + canvasExpr.toString(e, debug);
            case 2 -> "all pixels of " + canvasExpr.toString(e, debug);
            default -> throw new IllegalStateException();
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        canvasExpr = (Expression<MapCanvas>) exprs[exprs.length - 1];
        if (canvasExpr == null && mapEditSection == null) {
            Skript.error("You need to specify a map canvas when outside a map edit section");
            return false;
        }
        pattern = matchedPattern | parseResult.mark;
        switch (matchedPattern) {
            case 0:
                numberExpr1 = (Expression<Number>) exprs[0];
                numberExpr2 = (Expression<Number>) exprs[1];
            case 1:
                numberExpr3 = (Expression<Number>) exprs[2];
                numberExpr4 = (Expression<Number>) exprs[3];
        }
        return true;
    }

    @Override
    public @Nullable
    Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return switch (mode) {
            case SET, DELETE, RESET -> CollectionUtils.array(Color.class, Number.class);
            default -> null;
        };
    }

    @Override
    public void change(@NotNull Event e, Object @Nullable [] delta, Changer.@NotNull ChangeMode mode) {
        boolean set = mode == Changer.ChangeMode.SET;
        MapCanvas canvas = canvasExpr == null ? mapEditSection.getCanvas() : canvasExpr.getSingle(e);
        
        if (canvas == null || (set && (delta == null || delta.length == 0))) return;
        
        byte pixel;
        if (!set) pixel = 0;
        else if (delta[0] instanceof Color color) pixel = Maps.matchColor(color.asBukkitColor().asRGB());
        else if (delta[0] instanceof Number number) pixel = number.byteValue();
        else return;
        
        switch (pattern) {
            case 0 -> {
                Number x = numberExpr1.getSingle(e), y = numberExpr2.getSingle(e);
                if (x != null && y != null)
                    canvas.setPixel(x.intValue() - 1, y.intValue() - 1, pixel);
            }
            case 1, 3 -> {
                Number x1 = numberExpr1.getSingle(e), y1 = numberExpr2.getSingle(e), x2 = numberExpr3.getSingle(e), y2 = numberExpr4.getSingle(e);
                if (x1 == null || y1 == null || x2 == null || y2 == null) return;
                int startX = x1.intValue() - 1, startY = y1.intValue() - 1, endX = x2.intValue() - 1, endY = y2.intValue() - 1;
                if (pattern == 1) {
                    plotLine(startX, startY, endX, endY, canvas, pixel);
                } else {
                    for (int x = startX; x <= endX; x++)
                        for (int y = startY; y <= endY; y++)
                            canvas.setPixel(x, y, pixel);
                }
            }
            case 2 -> {
                byte[] mapBuffer = Maps.getBuffer(canvas);
                Arrays.fill(mapBuffer,1, mapBuffer.length-1, pixel);
                canvas.setPixel(0, 0, pixel);
                canvas.setPixel(127, 127, pixel);
            }
        }
        Maps.checkClearColorTable();
    }

    private void plotLine(int x0, int y0, int x1, int y1, MapCanvas canvas, byte pixel) {
        int
                dx = Math.abs(x1 - x0),
                dy = -Math.abs(y1 - y0),
                sx = x0 < x1 ? 1 : -1,
                sy = y0 < y1 ? 1 : -1,
                err = dx + dy;
        for (int e2 = 2 * err; x0 != x1 || y0 != y1; e2 = 2 * err) {
            canvas.setPixel(x0, y0, pixel);
            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            }
            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
}