package com.lotzy.skcrew.spigot.files.effects;

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
import java.nio.file.StandardCopyOption;
import org.bukkit.event.Event;

@Name("Files - Move")
@Description("Move file or directory")
@Examples({"on load:",
        "\tmove file \"eula.txt\" to file \"plugins/\""})
@Since("1.0")
public class EffMoveFileDir extends Effect {

    static {
        Skript.registerEffect(EffMoveFileDir.class,
            "move %paths% to %path%",
            "move %paths% to %path% with (overwrit|replac)(e|ing)");
    }
    
    private Expression<Path> paths;
    private Expression<Path> target;
    private Boolean shouldOverwrite = false;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        paths = (Expression<Path>) expr[0];
        target = (Expression<Path>) expr[1];
        shouldOverwrite = matchedPattern == 1; 
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "move " + paths.toString(e, debug) + " to " + target.toString(e, debug) + (shouldOverwrite ? " replacing existing ones" : "");
    }
 
    @Override
    protected void execute(Event e) {
       Path[] pathsList = paths.getArray(e);
        Path targetFile = target.getSingle(e);
        for (Path path : pathsList) {
            if (Files.isDirectory(targetFile)) {
                targetFile = Paths.get(targetFile.toString() + File.pathSeparator + path.getFileName()).normalize();
            }
            Path destFile = targetFile;
            destFile.getParent().toFile().mkdirs();
            try {
                if (shouldOverwrite) {
                    Files.move(path, destFile, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Files.move(path, destFile);
                }
            } catch (IOException ex) { }
        }
    }
}
