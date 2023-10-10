package com.lotzy.skcrew.spigot.bitwise;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class ExprNumber extends SimpleExpression<Number> {

    private String regex;
    private int pattern;

    static {
        Skript.registerExpression(
                ExprNumber.class,
                Number.class,
                ExpressionType.SIMPLE,
                "0(x|X)<[A-Fa-f0-9]+>",
                "0(b|B)<[0-1]+>"
        );
    }

    @Override
    protected Number[] get(Event event) {
        if(regex == null) return new Number[0];
        Number result = Integer.parseInt(regex, pattern == 0 ? 16 : 2);
        return new Number[]{result};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return (pattern == 0 ? "hexadecimal " : "binary ") + regex;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        pattern = i;
        regex = parseResult.regexes.get(0).group();
        return true;
    }
}