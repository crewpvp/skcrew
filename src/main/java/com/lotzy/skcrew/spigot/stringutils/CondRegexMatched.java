package com.lotzy.skcrew.spigot.stringutils;

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
import java.util.regex.Pattern;


@Name("StringUtils - Regex is matched")
@Description("Check text is matched to regular expression")
@Examples({"if \"1361\" is regex matches \"1..1\":",
        "\tbroadcast \"yes\""})
@Since("1.0")
public class CondRegexMatched extends Condition {

    static {
        Skript.registerCondition(CondRegexMatched.class,
            "%string% [(does|is)] regex match(es|ed) %string%",
            "%string% (does|is)(n't| not) regex match(es|ed) %string%");
    }

    private Expression<String> expr1;
    private Expression<String> expr2;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        expr1 = (Expression<String>) exprs[0];
        expr2 = (Expression<String>) exprs[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        return isNegated() != Pattern.matches(expr2.getSingle(e), expr1.getSingle(e));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Regex matches of "+expr1.toString(e,debug)+" to "+expr2.toString(e,debug);
    }
}