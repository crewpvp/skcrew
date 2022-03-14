package com.lotzy.skcrew.map.Image.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ExprImageChat extends PropertyExpression<BufferedImage, String> {

    static {
        Skript.registerExpression(ExprImageChat.class, String.class, ExpressionType.PROPERTY,
                "%image% as [chat] (string|message|text) [occupying %-number% lines]");
    }

    private Expression<Number> numExpr;

    @Override
    protected String[] get(@NotNull Event e, BufferedImage @NotNull [] source) {
        BufferedImage image = getExpr().getSingle(e);
        Number lines = numExpr == null ? 10 : numExpr.getSingle(e);
        if (image == null || lines == null) return null;
        return imageToText(image, lines.intValue());
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return getExpr().toString(e, debug) + " as chat string" + (numExpr == null ? "" : " occupying " + numExpr.toString(e, debug) + " lines");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        setExpr((Expression<? extends BufferedImage>) exprs[0]);
        numExpr = (Expression<Number>) exprs[1];
        return true;
    }

    private static String[] imageToText(BufferedImage image, int height) {
        double scale = image.getHeight() / (double) height;
        int width = (int) (image.getWidth() / scale), scaleInt = (int) scale;
        String[] imagePixels = new String[height];
        for (int y = 0; y < height; y++) {
            int pixelY = scaleInt * y;
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < width; x++)
                line.append(ChatColor.of(new Color(image.getRGB(x * scaleInt, pixelY)))).append('█');
            imagePixels[y] = line.toString();
        }
        return imagePixels;
    }

}