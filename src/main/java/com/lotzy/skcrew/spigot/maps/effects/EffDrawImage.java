package com.lotzy.skcrew.spigot.maps.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.skcrew.spigot.maps.Map;
import org.bukkit.event.Event;

import java.awt.image.BufferedImage;
import java.io.File;

public class EffDrawImage extends Effect {

    static {
        Skript.registerEffect(EffDrawImage.class, "draw image %string% on [[the] map] %map%");
    }

    private Expression<String> image;
    private Expression<Map> map;
    
    @Override
    protected void execute(Event e) {
        File imageFile = new File(Skcrew.getInstance().getDataFolder(), "images/" + image.getSingle(e));
        if (!imageFile.exists()) {
            Skript.warning("Image file does not exist!");
            return;
        }
        BufferedImage image = null;
        try {
            image = javax.imageio.ImageIO.read(imageFile);
        } catch (Exception ex) {
            Skript.warning("Error reading image file!");
            return;
        }
        map.getSingle(e).drawImage(image);
    }

    @Override
    public String toString( Event e, boolean debug) {
        return null;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.image = (Expression<String>) exprs[0];
        this.map = (Expression<Map>) exprs[1];
        return true;
    }
}
