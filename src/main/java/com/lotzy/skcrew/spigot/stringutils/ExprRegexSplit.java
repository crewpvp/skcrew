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


@Name("StringUtils - Regex split")
@Description("Split text using regular expression")
@Examples({"set {_text::*} to regex split \"123123123123\" at \".3\""})
@Since("1.0")
public class ExprRegexSplit extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprRegexSplit.class, String.class, ExpressionType.COMBINED, 
                "regex split %string% at %string%");
    }
    
    Expression<String> expr1;
    Expression<String> expr2;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        expr1 = (Expression<String>) exprs[0];
        expr2 = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected String[] get(Event e) {
       return expr1.getSingle(e).split(expr2.getSingle(e));
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
        return "Regex split "+expr1.toString(e,debug)+" at "+expr2.toString(e,debug);
    }
}