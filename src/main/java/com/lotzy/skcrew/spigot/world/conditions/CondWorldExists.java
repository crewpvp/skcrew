package com.lotzy.skcrew.spigot.world.conditions;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

@Name("World - World exists")
@Description("Check if world exists")
@Examples({"on load:",
        "\tbroadcast \"%world \"world\" is exists%\""})
@Since("1.0")
public class CondWorldExists extends Condition {

    static {
        Skript.registerCondition(CondWorldExists.class,
            "world %string% [(does|is)] (exist[s]|available)",
            "world %string% (does|is)(n't| not) (exist[s]|available)");
    }

    private Expression<String> expr;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        expr = (Expression<String>) exprs[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        Path p = Paths.get(expr.getSingle(e)+"/level.dat");
        return isNegated() != (Files.exists(p) ? Files.isRegularFile(p) : Pattern.compile("\\.\\w+$").matcher(p.toString()).find());
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "world " + expr.toString(e, debug) + " is " + (isNegated() ? "n't exist" : " exists");
    }
}