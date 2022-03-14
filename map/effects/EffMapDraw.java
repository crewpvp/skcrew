package com.lotzy.skcrew.map.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.map.Maps;
import com.lotzy.skcrew.map.sections.SecMapEdit;
import org.bukkit.event.Event;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MinecraftFont;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.image.BufferedImage;

public class EffMapDraw extends Effect {

    static {
        Skript.registerEffect(EffMapDraw.class,
                "draw image %image% [at " + Maps.coordPattern(true) + "] [on %-canvasmap%]",
                "draw text %string% at " + Maps.coordPattern(false) + " [on %-canvasmap%]");
    }

    private SecMapEdit mapEditSection;

    private Expression<BufferedImage> imageExpr;
    private Expression<Number> numberExpr1, numberExpr2;
    private Expression<MapCanvas> canvasExpr;
    private Expression<String> stringExpr;

    private boolean drawImage;

    @Override
    protected void execute(@NotNull Event e) {
        MapCanvas canvas = canvasExpr == null ? mapEditSection.getCanvas() : canvasExpr.getSingle(e);
        Number x = numberExpr1 == null ? 0 : numberExpr1.getSingle(e), y = numberExpr2 == null ? 0 : numberExpr2.getSingle(e);
        if (canvas == null || x == null || y == null) return;
        if (drawImage) {
            BufferedImage image = imageExpr.getSingle(e);
            if (image != null)
                Maps.imageToMapPixels(image, x.intValue(), y.intValue(), canvas::setPixel);
        } else {
            String text = stringExpr.getSingle(e);
            if (text != null)
                canvas.drawText(x.intValue(), y.intValue(), MinecraftFont.Font, Maps.toMapFormat(text));
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "draw " + (drawImage ? "image " + imageExpr.toString(e, debug) : "text " + stringExpr.toString(e, debug)) + " at (" + numberExpr1.toString(e, debug) + ", " + numberExpr2.toString(e, debug) + ") on " + canvasExpr.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        canvasExpr = (Expression<MapCanvas>) exprs[3];
        if (canvasExpr == null && mapEditSection == null) {
            Skript.error("You need to specify a map canvas when outside a map edit section");
            return false;
        }
        if (drawImage = (matchedPattern == 0))
            imageExpr = (Expression<BufferedImage>) exprs[0];
        else
            stringExpr = (Expression<String>) exprs[0];
        numberExpr1 = (Expression<Number>) exprs[1];
        numberExpr2 = (Expression<Number>) exprs[2];
        return true;
    }
}
