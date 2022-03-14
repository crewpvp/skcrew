package com.lotzy.skcrew.map.Image.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.map.Image.ImageUtils;
import com.lotzy.skcrew.map.Maps;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.image.BufferedImage;


public class EffEditImage extends Effect {

    static {
        Skript.registerEffect(EffEditImage.class,
                "draw [a] %color% ([(4¦outline of [a])] (rectangle|1¦oval)|2¦line) (between|from) " + Maps.coordPattern(false) + " (and|to) " + Maps.coordPattern(false) + " on %image%",
                "(clear|crop) area (between|from) " + Maps.coordPattern(false) + " (and|to) " + Maps.coordPattern(false) + " on %image%",
                "draw image %image% at " + Maps.coordPattern(false) + " on image %image%");
    }

    private Expression<Color> colorExpr;
    private Expression<Number> numberExpr1, numberExpr2, numberExpr3, numberExpr4;
    private Expression<BufferedImage> imageExpr1, imageExpr2;

    private int pattern, shapeOptions;

    @Override
    protected void execute(@NotNull Event e) {
        BufferedImage image = imageExpr1.getSingle(e);
        Number x0 = numberExpr1.getSingle(e), y0 = numberExpr2.getSingle(e);
        if (image == null || x0 == null || y0 == null) return;
        int x = x0.intValue(), y = y0.intValue();
        switch (pattern) {
            case 0 -> {
                Color color = colorExpr.getSingle(e);
                Number x1 = numberExpr3.getSingle(e), y1 = numberExpr4.getSingle(e);
                if (color == null || x1 == null || y1 == null) return;
                int endX = x1.intValue(), endY = y1.intValue(), width = endX - x,  height = endY - y;
                ImageUtils.editGraphics(image, g2d -> {
                    g2d.setColor(new java.awt.Color(color.asBukkitColor().asRGB()));
                    int shape = shapeOptions & 3;
                    if (shape == 2) {
                        g2d.drawLine(x, y, endX, endY);
                    } else if (shapeOptions >>> 2 == 0) {
                        if (shape == 0) g2d.fillRect(x, y, width, height);
                        else g2d.fillOval(x, y, width, height);
                    } else {
                        if (shape == 0) g2d.drawRect(x, y, width, height);
                        else g2d.drawOval(x, y, width, height);
                    }
                });
            }
            case 1 -> {
                Number x1 = numberExpr3.getSingle(e), y1 = numberExpr4.getSingle(e);
                if (x1 != null && y1 != null)
                    ImageUtils.editGraphics(image, g2d -> g2d.clearRect(x, y, x1.intValue() - x, y1.intValue() - y));
            }
            case 2 -> {
                BufferedImage copy = imageExpr2.getSingle(e);
                if (copy != null)
                    ImageUtils.drawImage(copy, image, x, y);
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return switch (pattern) {
            case 0 -> "draw a " + colorExpr.toString(e, debug) + (shapeOptions >>> 2 == 0 ? " " : " outline of a ") + (switch (shapeOptions & 3) {
                case 0 -> "rectangle";
                case 1 -> "oval";
                default -> "line";
            }) + " from (" + numberExpr1.toString(e, debug) + ", " + numberExpr2.toString(e, debug) + ") to (" + numberExpr3.toString(e, debug) + ", " + numberExpr4.toString(e, debug) + ") on " + imageExpr1.toString(e, debug);
            case 1 -> "clear area between (" + numberExpr1.toString(e, debug) + ", " + numberExpr2.toString(e, debug) + ") and (" + numberExpr3.toString(e, debug) + ", " + numberExpr4.toString(e, debug) + ") on " + imageExpr1.toString(e, debug);
            case 2 -> "draw image " + imageExpr2.toString(e, debug) + " at (" + numberExpr1.toString(e, debug) + ", " + numberExpr2.toString(e, debug) + ") on image " + imageExpr1.toString(e, debug);
            default -> throw new IllegalStateException();
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        pattern = matchedPattern;
        imageExpr1 = (Expression<BufferedImage>) exprs[exprs.length-1];

        int pos = 1 - (matchedPattern & 1);
        numberExpr1 = (Expression<Number>) exprs[pos];
        numberExpr2 = (Expression<Number>) exprs[pos + 1];

        switch (matchedPattern) {
            case 0 -> {
                shapeOptions = parseResult.mark;
                colorExpr = (Expression<Color>) exprs[0];
                numberExpr3 = (Expression<Number>) exprs[3];
                numberExpr4 = (Expression<Number>) exprs[4];
            }
            case 1 -> {
                numberExpr3 = (Expression<Number>) exprs[2];
                numberExpr4 = (Expression<Number>) exprs[3];
            }
            case 2 -> imageExpr2 = (Expression<BufferedImage>) exprs[0];
        }
        return true;
    }
}
