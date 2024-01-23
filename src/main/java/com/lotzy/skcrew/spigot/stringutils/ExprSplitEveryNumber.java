package com.lotzy.skcrew.spigot.stringutils;

import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;


@Name("StringUtils - Split text")
@Description("Split text every number of symbols")
@Examples({"set {_text::*} to \"123123123123\" split every 3 symbols"})
@Since("1.0")
public class ExprSplitEveryNumber extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprSplitEveryNumber.class, String.class, ExpressionType.COMBINED, 
                "%string% split [(by|at)] every %integer% (symbol|char[acter])[s]",
                "split %string% [(by|at)] every %integer% (symbol|char[acter])[s]");
    }
    
    Expression<String> expr1;
    Expression<Integer> expr2;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        expr1 = (Expression<String>) exprs[0];
        expr2 = (Expression<Integer>) exprs[1];
        return true;
    }

    @Override
    protected String[] get(Event e) {
        String input = expr1.getSingle(e);
        Integer number = expr2.getSingle(e);
        if (number <= 0) { return new String[] { input }; }
	return input.split("(?<=\\G.{" + number + "})");
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
        return "Split every "+expr2.toString(e,debug)+" characters in "+expr1.toString(e,debug);
    }
}