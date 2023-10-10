package com.lotzy.skcrew.spigot.files.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import org.bukkit.event.Event;

@Name("Files - is Directory")
@Description("Check if file is directory")
@Examples({"on load:",
        "\tif file \"eula.txt\" is directory:",
        "\t\tbroadcast \"yes\""})
@Since("1.0")
public class CondFileIsDir extends Condition {

    static {
        Skript.registerCondition(CondFileIsDir.class,
            "%path% is dir[ectory]","%path% is(n't| not) dir[ectory]");
    }
    private Expression<Path> path;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        path = (Expression<Path>) expr[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        Path p = path.getSingle(e);
        return isNegated()!= (Files.exists(p) ? Files.isDirectory(p) : !Pattern.compile("\\.\\w+$").matcher(p.toString()).find());
    }

    @Override
    public String toString(Event e, boolean debug) {
        return path.toString(e, debug) + " is " + (isNegated() ? " not" : "" ) + " directory";
    }
}
