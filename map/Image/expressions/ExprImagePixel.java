package com.lotzy.skcrew.map.Image.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.ColorRGB;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.map.Maps;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.image.BufferedImage;

public class ExprImagePixel extends PropertyExpression<BufferedImage, Color> {

    static {
        Skript.registerExpression(ExprImagePixel.class, Color.class, ExpressionType.PROPERTY,
                "image (pixel|color) at " + Maps.coordPattern(false) + " (on|of) %image%");
    }

    private Expression<Number> numberExpr1, numberExpr2;

    @Override
    protected Color[] get(@NotNull Event e, BufferedImage @NotNull [] source) {
        Number x = numberExpr1.getSingle(e), y = numberExpr2.getSingle(e);
        if (x == null || y == null) return null;
        int pixelX = x.intValue(), pixelY = y.intValue();
        return get(source, image -> {
            try {
                java.awt.Color color = new java.awt.Color(image.getRGB(pixelX, pixelY));
                return new ColorRGB(color.getRed(), color.getBlue(), color.getGreen());
            } catch (Exception ex) {
                return null;
            }
        });
    }

    @Override
    public @NotNull Class<? extends Color> getReturnType() {
        return Color.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "image pixel at (" + numberExpr1.toString(e, debug) + ", " + numberExpr2.toString(e, debug) + ") on " + getExpr().toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        numberExpr1 = (Expression<Number>) exprs[0];
        numberExpr2 = (Expression<Number>) exprs[1];
        setExpr((Expression<? extends BufferedImage>) exprs[2]);
        return true;
    }

    @Override
    public @Nullable
    Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? CollectionUtils.array(Color.class) : null;
    }

    @Override
    public void change(@NotNull Event e, Object @Nullable [] delta, Changer.@NotNull ChangeMode mode) {
        BufferedImage image = getExpr().getSingle(e);
        Number x = numberExpr1.getSingle(e), y = numberExpr2.getSingle(e);
        if (delta == null || delta.length == 0 || !(delta[0] instanceof Color color) || image == null || x == null || y == null) return;
        int pixelX = x.intValue(), pixelY = y.intValue();
        try {
            image.setRGB(pixelX, pixelY, color.asBukkitColor().asRGB());
        } catch (Exception ignored) {}
    }

}
