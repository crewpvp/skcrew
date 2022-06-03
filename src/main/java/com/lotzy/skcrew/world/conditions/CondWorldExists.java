package com.lotzy.skcrew.world.conditions;

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
import java.io.File;

@Name("World - World exists")
@Description("Check if world exists")
@Examples({"on load:",
        "\tbroadcast \"%world \"world\" exists%\""})
@Since("1.0")
public class CondWorldExists extends Condition {

    static {
        Skript.registerCondition(CondWorldExists.class,
            "world %string% [is] exist[s]",
            "world %string% (does|is)(n't| not) exist[s]");
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
        return isNegated() != (new File("./"+expr.getSingle(e) + "/level.dat")).isFile();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "world " + expr.toString(e, debug) + " is " + (isNegated() ? "n't exist" : " exists");
    }

}