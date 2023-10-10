package com.lotzy.skcrew.spigot.world.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.event.Event;

@Name("World - Load world")
@Description("Load world by name")
@Examples({"command /loadworld <text>:",
        "\ttrigger:",
        "\t\tload world arg-1"})
@Since("1.0")
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
        return "Loading world: "+expr.toString(e, debug);
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
