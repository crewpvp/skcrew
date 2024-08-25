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
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.bukkit.event.Event;

@Name("Files - Rename")
@Description("Rename file or directory")
@Examples({"on load:",
        "\trename file \"eula.txt\" to \"noteula.txt\""})
@Since("1.0")
public class EffRenameFileDir extends Effect {

    static {
        Skript.registerEffect(EffRenameFileDir.class, 
            "rename %paths% to %string%",
            "rename %paths% to %string% with (overwrit|replac)(e|ing)"
        );
    }
    
    private Expression<Path> paths;
    private Expression<String> name;
    private Boolean shouldOverwrite;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        paths = (Expression<Path>) expr[0];
        name = (Expression<String>) expr[1];
        shouldOverwrite = matchedPattern == 1;
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "rename " + paths.toString(e, debug) + " to " + name.toString(e, debug) + (shouldOverwrite ? " replacing existing ones" : "");
    }
 
    @Override
    protected void execute(Event e) {
        Path[] pathsList = paths.getArray(e);
        String currentName = name.getSingle(e);
        for (Path path : pathsList) {
            if (Files.exists(path)) {
                try {
                    if (shouldOverwrite) {
                        Files.move(path, path.resolveSibling(currentName), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        try {
                            Files.move(path, path.resolveSibling(currentName));
                        } catch (FileAlreadyExistsException ex) {
                            Skript.warning(path.toString()+" already exists and doesn't renamed");
                        }
                    }
                } catch (IOException ex) {}
            }
        }
    }
}
