package com.lotzy.skcrew.spigot.maps.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.maps.Map;
import org.bukkit.event.Event;
import java.awt.*;

public class EffDrawText extends Effect {
    
    static {
        Skript.registerEffect(EffDrawText.class, "draw text %string% [at] %number%(,[ ]| )%number% "
                + "[with font %-string%] [with size %-number%] [with colo[u]r] "
                + "%number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%");
    }

    private Expression<String> text;
    private Expression<Number> x;
    private Expression<Number> y;
    private Expression<String> font;
    private Expression<Number> size;
    private Expression<Number> R;
    private Expression<Number> G;
    private Expression<Number> B;
    private Expression<Number> A;
    private Expression<Map> map;

    @Override
    protected void execute( Event e) {
        String text = this.text.getSingle(e);
        String font = this.font != null ? this.font.getSingle(e) : "Arial";
        int size = this.size != null ? this.size.getSingle(e).intValue() : 12;
        int alpha = A != null ? A.getSingle(e).intValue() : 255;
        
        Font drawFont = new Font(font, Font.PLAIN, size);
        Color color = new Color(R.getSingle(e).intValue(),G.getSingle(e).intValue(), B.getSingle(e).intValue(), alpha);
        map.getSingle(e).drawText(text, x.getSingle(e).intValue(),y.getSingle(e).intValue(),color, drawFont);
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "draw text";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.text = (Expression<String>) exprs[0];
        this.x = (Expression<Number>) exprs[1];
        this.y = (Expression<Number>) exprs[2];
        this.font = (Expression<String>) exprs[3];
        this.size = (Expression<Number>) exprs[4];
        this.R = (Expression<Number>) exprs[6];
        this.G = (Expression<Number>) exprs[7];
        this.B = (Expression<Number>) exprs[8];
        this.A = (Expression<Number>) exprs[9];
        this.map = (Expression<Map>) exprs[10];
        return true;
    }
}

