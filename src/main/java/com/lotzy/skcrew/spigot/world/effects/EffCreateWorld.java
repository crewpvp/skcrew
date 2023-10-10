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
import org.bukkit.WorldType;
import org.bukkit.event.Event;

@Name("World - Create world")
@Description("Create default world or superflat with specifed name")
@Examples({"command /createworld <text>:",
        "\ttrigger:",
        "\t\tcreate world arg-1"})
@Since("1.0")
public class EffCreateWorld extends Effect {
    static {
        Skript.registerEffect(EffCreateWorld.class,
            "create world %string%",
            "create world %string% [with type] [super]flat");
    }

    private Expression<String> expr;
    private Boolean superflat;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        expr = (Expression<String>) exprs[0];
        superflat = matchedPattern == 1;
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "Creating world: "+expr.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
        if (!(new File(expr.getSingle(e)+"/level.dat")).isFile()) {
            if (!superflat) {
                Bukkit.getServer().createWorld(new WorldCreator(expr.getSingle(e)));
            } else {
                Bukkit.getServer().createWorld(new WorldCreator(expr.getSingle(e)).type(WorldType.FLAT));
            }
        } else { Skript.warning("World "+expr.getSingle(e)+" exists"); }
    }
}
