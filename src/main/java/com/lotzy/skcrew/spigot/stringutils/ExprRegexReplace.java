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

@Name("StringUtils - Regex replace")
@Description("Replace text using regular expression")
@Examples({"set {_text} to regex replace \"3.*\" with \"\" in \"123123123123\""})
@Since("1.0")
public class ExprRegexReplace extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprRegexReplace.class, String.class, ExpressionType.COMBINED, 
            "regex replace %string% with %string% in %string%");
    }
    
    Expression<String> expr1;
    Expression<String> expr2;
    Expression<String> expr3;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        expr1 = (Expression<String>) exprs[0];
        expr2 = (Expression<String>) exprs[1];
        expr3 = (Expression<String>) exprs[2];
        return true;
    }

    @Override
    protected String[] get(Event e) {
       return new String[] { expr3.getSingle(e).replaceAll(expr1.getSingle(e), expr2.getSingle(e)) };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Regex replace "+expr1.toString(e,debug)+" with "+expr2.toString(e,debug)+ " in "+expr3.toString(e,debug);
    }
}