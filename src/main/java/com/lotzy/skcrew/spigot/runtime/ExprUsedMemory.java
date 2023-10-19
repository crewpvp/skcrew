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


@Name("Runtime - Used memory")
@Description("Return currently used bytes of allocated memory of jvm")
@Examples({"on load:",
        "\tbroadcast \"%used memory of server%\""})
@Since("1.0")
public class ExprUsedMemory extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprUsedMemory.class, Number.class,
                ExpressionType.SIMPLE, "[the] [server['s]] used memory",
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
    public String toString(final  Event e, boolean debug) {
        return "used memory of server";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        return true;
    }
}
