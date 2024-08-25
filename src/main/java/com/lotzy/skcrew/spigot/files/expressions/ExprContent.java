package com.lotzy.skcrew.spigot.files.expressions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import javax.swing.event.ChangeEvent;

@Name("Files - File content")
@Description("Set or get content of file")
@Examples({"on load:",
        "\tset {_file} to content of file \"eula.txt\"",
        "\treplace all \"false\" with \"true\" in {_file}",
        "\tset content of file \"eula.txt\" to {_file}"})
@Since("1.0")
public class ExprContent extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprContent.class, String.class, ExpressionType.COMBINED,
            "[the] content of %path%",
            "[the] %path%'s content"
        );
    }

    private Expression<Path> path;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        path = (Expression<Path>) expr[0];
        return true;
    }

    @Override
    protected String[] get(Event e) {
        Path currentPath = path.getSingle(e);
        if (Files.isRegularFile(currentPath)) {
            try {
                List<String> lines = Files.readAllLines(currentPath);
                return lines.toArray(new String[lines.size()]);
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return new String[0];
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        switch (mode) {
            case SET:
            case ADD:
            case REMOVE:
                return new Class[]{String.class};
            default:
                break;
        }
        return new Class[0];
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        Path currentPath = path.getSingle(e);
        if (Files.isRegularFile(currentPath)) {
            try {
                List<String> edits = new LinkedList<>();
                for (Object o : delta) {
                    if (o instanceof String) {
                        edits.add((String) o);
                    }
                }
                switch (mode) {
    
                    case SET:
                        Files.write(currentPath, edits, StandardCharsets.UTF_8);
                        new ChangeEvent(currentPath);
                        break;
    
                    case ADD:
                        Files.write(currentPath, edits, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                        new ChangeEvent(currentPath);
                        break;
    
                    case REMOVE:
                        List<String> newLines = new LinkedList<>();
                        List<String> lines = Files.readAllLines(currentPath, StandardCharsets.UTF_8);
                        for (String edit : edits) {
                            for (String line : lines) {
                                line = line.replaceAll(edit, "").trim();
                                if (!line.isEmpty()) {
                                    newLines.add(line);
                                }
                            }
                        }
                        Files.write(currentPath, newLines, StandardCharsets.UTF_8);
                        new ChangeEvent(currentPath);
                        break;
                        
                    default:
                        break;
                }
    
            } catch (IOException ex) { }
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "content of " + path.toString(e, debug);
    }

}
