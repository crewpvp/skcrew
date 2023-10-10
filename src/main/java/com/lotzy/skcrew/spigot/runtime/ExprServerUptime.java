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
import java.lang.management.ManagementFactory;
import org.bukkit.event.Event;
import ch.njol.skript.util.Timespan;

@Name("Runtime - Server uptime")
@Description("Return timespan with server working time")
@Examples({"on load:",
        "\tbroadcast \"%uptime of server%\""})
@Since("1.0")
public class ExprServerUptime extends SimpleExpression<Timespan> {

    static {
        Skript.registerExpression(ExprServerUptime.class, Timespan.class,
                ExpressionType.SIMPLE, "[the] uptime of server",
                        "[the] server['s] uptime");
    }

    @Override
    protected Timespan[] get(final Event e) {
        return new Timespan[] {new Timespan(ManagementFactory.getRuntimeMXBean().getUptime())};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    public String toString(final  Event e, boolean debug) {
        return "Server uptime";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        return true;
    }
}
