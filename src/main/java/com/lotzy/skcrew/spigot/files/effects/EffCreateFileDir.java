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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.event.Event;

@Name("Files - Create file dir")
@Description("Create directory or file")
@Examples({"on load:",
        "\tcreate file \"eula.txt\" with content \"eula=true\""})
@Since("1.0")
public class EffCreateFileDir extends Effect {

    static {
        Skript.registerEffect(EffCreateFileDir.class,
            "create %paths%",
            "create %paths% with [(text|string|content)] %strings%");
    }
    
    private Expression<Path> paths;
    private Expression<String> content;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        paths = (Expression<Path>) expr[0];
        if (matchedPattern == 1) {
            content = (Expression<String>) expr[1];
        }
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "create " + paths.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
       Path[] pathsList = paths.getArray(e);
        String[] currentContent = content != null ? content.getArray(e) : new String[]{""};
        for (Path path : pathsList) {
            try {
                if (Pattern.compile("\\.\\w+$").matcher(path.toString()).find()) {
                    Files.createDirectories(path.getParent());
                    if (content != null) {
                        List<String> lines = Arrays.asList(currentContent);
                        Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
                    } else if (!Files.exists(path)) {
                        Files.createFile(path);
                    }
                } else if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
            } catch (IOException ex) {}
        }
    }
}