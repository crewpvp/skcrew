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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;

@Name("World - Unload world")
@Description("Unload specifed world")
@Examples({"on disconnect:",
        "\tunload player's world"})
@Since("1.0")
public class EffUnloadWorld extends Effect {
    static {
        Skript.registerEffect(EffUnloadWorld.class,
            "unload [world] %world%",
            "unload [world] %world% without save");
    }

    private Expression<World> expr;
    private Boolean unsave;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        expr = (Expression<World>) exprs[0];
        unsave = matchedPattern == 1;
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "Unloading world "+expr.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
        World world = expr.getSingle(e);
        if (Bukkit.getServer().getWorlds().contains(world)) {
            World mainWorld = Bukkit.getWorlds().get(0);
            if (world!=mainWorld) {
                world.getPlayers().forEach(player -> player.teleport(mainWorld.getSpawnLocation()));
                Bukkit.getServer().unloadWorld(world, unsave);
            }
        }
    } 
}
