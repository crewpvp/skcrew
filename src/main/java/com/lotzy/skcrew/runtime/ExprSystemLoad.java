package com.lotzy.skcrew.runtime;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import java.lang.management.ManagementFactory;
import org.bukkit.event.Event;
import javax.annotation.Nullable;

public class ExprSystemLoad extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprSystemLoad.class, Number.class,
                ExpressionType.SIMPLE, "[the] system'[s] average load",
                        "average load of system");
    }

    @Override
    protected Number[] get(final Event e) {
        return new Number[] {( ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage() / Runtime.getRuntime().availableProcessors()) * 100};
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
        return "Average load of current machine";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        return true;
    }
}
