package com.lotzy.skcrew.runtime;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import javax.annotation.Nullable;

public class ExprUsedMemory extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprUsedMemory.class, Number.class,
                ExpressionType.SIMPLE, "[the] [server'[s]] used memory",
                        "used memory of server");
    }

    @Override
    protected Number[] get(final Event e) {
        return new Number[] {Runtime.getRuntime().totalMemory() - (Runtime.getRuntime().freeMemory())};
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
    public String toString(final @Nullable Event e, boolean debug) {
        return "used memory of server";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        return true;
    }
}
