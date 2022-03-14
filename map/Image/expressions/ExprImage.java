package com.lotzy.skcrew.map.Image.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Converters;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.map.Image.ImageUtils;
import com.lotzy.skcrew.map.Maps;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;


public class ExprImage extends SimpleExpression<BufferedImage> {

    static {
        Skript.registerExpression(ExprImage.class, BufferedImage.class, ExpressionType.COMBINED,
                "[new] image from (file [path]|1¦url) %string%",
                "images (from|in) folder %string%",
                "%images% (resized|1¦scaled)[ (to|by) " + Maps.coordPattern(true) + "]",
                "%images% flipped (horizontally|1¦vertically)",
                "sub[ ]image of %images% from " + Maps.coordPattern(false) + " to " + Maps.coordPattern(false),
                "new (blank|empty|) image [with dimensions " + Maps.coordPattern(true) + "]");
    }

    private int pattern;
    private boolean option;

    private Expression<String> stringExpr;
    private Expression<BufferedImage> imageExpr;
    private Expression<Number> numberExpr1, numberExpr2, numberExpr3, numberExpr4;

    @Override
    protected @Nullable
    BufferedImage[] get(@NotNull Event e) {
        return switch (pattern) {
            case 0 -> {
                String path = stringExpr.getSingle(e);
                if (path != null)
                    try {
                        yield CollectionUtils.array(option ? ImageIO.read(new File(path)) : ImageIO.read(new URL(path)));
                    } catch (Exception ignore) {}
                yield null;
            }
            case 1 -> {
                String path = stringExpr.getSingle(e);
                if (path != null) {
                    File folder = new File(path);
                    if (folder.isDirectory()) {
                        Collection<BufferedImage> collector = new ArrayList<>();
                        searchFolder(folder, collector);
                        yield collector.toArray(BufferedImage[]::new);
                    }
                }
                yield null;
            }
            case 2 -> {
                if (numberExpr1 == null || numberExpr2 == null)
                    yield Converters.convert(imageExpr.getArray(e), BufferedImage.class, image -> ImageUtils.resizeImage(image, 128, 128));
                Number x = numberExpr1.getSingle(e), y = numberExpr2.getSingle(e);
                if (x == null || y == null) yield null;
                yield Converters.convert(imageExpr.getArray(e), BufferedImage.class, image -> option ? ImageUtils.resizeImage(image, x.intValue() * image.getWidth(), y.intValue() * image.getHeight()) : ImageUtils.resizeImage(image, x.intValue(), y.intValue()));
            }
            case 3 -> Converters.convert(imageExpr.getArray(e), BufferedImage.class, option ? image -> ImageUtils.resizeImage(image, -image.getWidth(), image.getHeight()) : image -> ImageUtils.resizeImage(image, image.getWidth(), -image.getHeight()));
            case 4 -> {
                Number x0 = numberExpr1.getSingle(e), y0 = numberExpr2.getSingle(e), x1 = numberExpr3.getSingle(e), y1 = numberExpr4.getSingle(e);
                if (x0 == null || y0 == null || x1 == null || y1 == null) yield null;
                int x = x0.intValue(), y = y0.intValue(), width = x1.intValue() - x, height = y1.intValue() - y;
                yield Converters.convert(imageExpr.getArray(e), BufferedImage.class, image -> {
                    try {
                        return image.getSubimage(x, y, width, height);
                    } catch (Exception ex) {
                        return null;
                    }
                });
            }
            case 5 -> {
                Number x = numberExpr1.getSingle(e), y = numberExpr2.getSingle(e);
                if (x == null || y == null) yield null;
                int width = x.intValue(), height = y.intValue();
                yield (width > 0 && height > 0) ? CollectionUtils.array(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)) : null;
            }
            default -> null;
        };
    }

    private void searchFolder(File folder, Collection<BufferedImage> collector) {
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File file : files)
            if (file.isDirectory())
                searchFolder(file, collector);
            else
                try { collector.add(ImageIO.read(file)); } catch (Exception ignored) {}
    }

    @Override
    public boolean isSingle() {
        return imageExpr == null ? pattern == 1 : imageExpr.isSingle();
    }

    @Override
    public @NotNull Class<? extends BufferedImage> getReturnType() {
        return BufferedImage.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return switch (pattern) {
            case 0 -> "image from " + (option ? "file " : "url ") + stringExpr.toString(e, debug);
            case 1 -> "images from folder " + stringExpr.toString(e, debug);
            case 2 -> imageExpr.toString(e, debug) + (option ? " resized" : " scaled") + (numberExpr1 == null || numberExpr2 == null ? "" : " to (" + numberExpr1.toString(e, debug) + ", " + numberExpr2.toString(e, debug) + ")");
            case 3 -> imageExpr.toString(e, debug) + " flipped " + (option ? "horizontally" : "vertically");
            case 4 -> "sub image of " + imageExpr.toString(e, debug) + " from (" + numberExpr1.toString(e, debug) + ", " + numberExpr2.toString(e, debug) + ") to (" + numberExpr3.toString(e, debug) + ", " + numberExpr4.toString(e, debug) + ")";
            case 5 -> "new empty image" + (numberExpr1 == null || numberExpr2 == null ? "" : " with dimensions (" + numberExpr1.toString(e, debug) + ", " + numberExpr2.toString(e, debug) + ")");
            default -> throw new IllegalStateException();
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        pattern = matchedPattern;
        option = parseResult.mark == 0;
        switch (matchedPattern) {
            case 0:
            case 1:
                stringExpr = (Expression<String>) exprs[0];
                break;
            case 4:
                numberExpr3 = (Expression<Number>) exprs[3];
                numberExpr4 = (Expression<Number>) exprs[4];
            case 2:
                numberExpr1 = (Expression<Number>) exprs[1];
                numberExpr2 = (Expression<Number>) exprs[2];
            case 3:
                imageExpr = (Expression<BufferedImage>) exprs[0];
                break;
            case 5:
                numberExpr1 = (Expression<Number>) exprs[0];
                numberExpr2 = (Expression<Number>) exprs[1];
        }
        return true;
    }
}
