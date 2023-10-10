package com.lotzy.skcrew.spigot.runtime;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

@Name("Runtime - Amount of processor cores")
@Description("Return amount of processor cores on the machine")
@Examples({"on load:",
        "\tbroadcast \"%amount of processor cores%\""})
@Since("1.0")
public class ExprProcessorCores extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprProcessorCores.class, Number.class,
                ExpressionType.SIMPLE, "processor['s] cores amount",
                        "[amount of] processor cores");
    }

    @Override
    protected Number[] get(final Event e) {
        return new Number[] { Runtime.getRuntime().availableProcessors() };
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
    public String toString(final  Event e, boolean debug) {
        return "Amount of processor cores";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        return true;
    }
}
