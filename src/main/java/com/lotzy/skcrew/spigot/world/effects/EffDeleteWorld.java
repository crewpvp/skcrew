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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;

@Name("World - Delete world")
@Description("Delete world by world type or world name")
@Examples({"command /deleteworld <text>:",
        "\ttrigger:",
        "\t\tdelete world arg-1"})
@Since("1.0")
public class EffDeleteWorld extends Effect {
    
    static {
        Skript.registerEffect(EffDeleteWorld.class,
            "delete world %world/string%");
    }

    private Expression<?> expr;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        expr = exprs[0];
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "Deleting world: "+expr.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
        final String wrld;
        World mainWorld = Bukkit.getWorlds().get(0);
        if (expr.getSingle(e) instanceof World) {
            World world = (World) expr.getSingle(e);
            if (world==mainWorld) return;
            world.getPlayers().forEach(player -> player.teleport(mainWorld.getSpawnLocation()));
            Bukkit.getServer().unloadWorld(world, false);
            wrld= world.getName();
        } else { 
            wrld = (String) expr.getSingle(e);
            World world = Bukkit.getWorld(wrld);
            if( world==mainWorld) return;
            if( world!=null) {
                world.getPlayers().forEach(player -> player.teleport(mainWorld.getSpawnLocation()));
                Bukkit.getServer().unloadWorld(world, false);
            }
        }
        Path path = Paths.get(wrld);
        if (Files.exists(path)) {
            try {
                if (Files.isDirectory(path)) {
                    Files.walk(path)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                } else {
                    Files.delete(path);
                }
            } catch (IOException ex) {}
        }
    }
}
