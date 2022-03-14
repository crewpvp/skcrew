package com.lotzy.skcrew.world.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.event.Event;

public class EffLoadWorld extends Effect {
    static {
        Skript.registerEffect(EffLoadWorld.class,
            "load world %string%");
    }

    private Expression<String> expr;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        expr = (Expression<String>) exprs[0];
        return true;
    }
    
    
    
    @Override
    public String toString(Event e, boolean debug) {
        return "Loading world "+expr.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
        String world = expr.getSingle(e);
        if ((new File(world+"/level.dat")).isFile()) {
            if(Bukkit.getWorld(world)==null)
                Bukkit.getServer().createWorld(new WorldCreator(world));
        } else { Skript.warning("World "+world+" doesn't exists"); }
    }
}
