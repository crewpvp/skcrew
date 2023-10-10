package com.lotzy.skcrew.spigot.files.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import org.bukkit.event.Event;
@Name("Files - File or Directory")
@Description("Return abstract file (path type) from string path")
@Examples({"on load:",
        "\tset {_file} to parent of file \"eula.txt\""})
@Since("1.0")
public class ExprFileDirectory extends SimpleExpression<Path> {

    static {
        Skript.registerExpression(ExprFileDirectory.class, Path.class, ExpressionType.COMBINED,
            "[the] (file[s]|dir[ector(y|ies)]) %strings%"
        );
    }

    private Expression<String> paths;
    private boolean isSingle;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        paths = (Expression<String>) expr[0];
        isSingle = paths.getSource().isSingle();
        return true;
    }

    @Override
    protected Path[] get(Event e) {
        LinkedList<Path> finalPaths = new LinkedList<>();
        String[] pathsList = paths.getArray(e);
        for (String path : pathsList) {
            if(path.isEmpty()) path = ".";
            finalPaths.add(Paths.get(path.replaceAll("^(?!((\\.)?\\.\\/))", "")));
        }
        return finalPaths.toArray(new Path[finalPaths.size()]);
    }

    @Override
    public boolean isSingle() {
        return isSingle;
    }

    @Override
    public Class<? extends Path> getReturnType() {
        return Path.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "file/directory " + paths.toString(e, debug);
    } 
}