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


@Name("StringUtils - Reverse text")
@Description("Mirror text")
@Examples({"set {_text} to reversed \"321\""})
@Since("1.0")
public class ExprReverseString extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprReverseString.class, String.class, ExpressionType.COMBINED, 
                "(reverse[d]|backward(s|ed)) [(string|text)] %string%");
    }
    
    Expression<String> expr1;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        expr1 = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected String[] get(Event e) {
       return new String[] { new StringBuilder(expr1.getSingle(e)).reverse().toString() };
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
        return "Reverse string "+expr1.toString(e,debug);
    }
}