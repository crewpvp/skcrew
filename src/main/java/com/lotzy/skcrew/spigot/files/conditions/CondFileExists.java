package com.lotzy.skcrew.spigot.files.conditions;

import java.nio.file.Files;
import java.nio.file.Path;
import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;

@Name("Files - is File exist")
@Description("Check your's file for existance on disk")
@Examples({"on load:",
        "\tif file \"eula.txt\" is exists:",
        "\t\tbroadcast \"yes\""})
@Since("1.0")
public class CondFileExists extends Condition {

    static {
        Skript.registerCondition(CondFileExists.class,
            "%path% (is|does) exist[s]","%path% (is|does)(n't| not) exist[s]");
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
        return isNegated() != Files.exists(path.getSingle(e));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return path.toString(e, debug) + " is " + (isNegated() ? " missing" : " available");
    }
}