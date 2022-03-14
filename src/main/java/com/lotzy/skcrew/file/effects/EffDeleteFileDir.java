package com.lotzy.skcrew.file.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import org.bukkit.event.Event;

public class EffDeleteFileDir extends Effect {

    static {
        Skript.registerEffect(EffDeleteFileDir.class, "delete %paths%");
    }
    private Expression<Path> paths;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        paths = (Expression<Path>) expr[0];
        return true;
    }
    
    
    
    @Override
    public String toString(Event e, boolean debug) {
        return "delete " + paths.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
       Path[] pathList = paths.getArray(e);
        for (Path path : pathList) {
            if (Files.exists(path)) {
                try {
                    if (Files.isDirectory(path)) {
                        Files.walk(path)
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete); // Can't use java nio here due to DirectoryNotEmptyException.
                    } else {
                        Files.delete(path);
                    }
                } catch (IOException ex) {
                    if (Files.exists(path)) {
                        Skript.exception(ex);
                    }
                }
            }
        }
 
    }
}
