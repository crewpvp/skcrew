package com.lotzy.skcrew.spigot.sockets.elements.expressions;

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
import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import org.bukkit.event.Event;


@Name("Sockets - Server")
@Description("Return server by name (com.lotzy.skcrew.shared.sockets.data.SpigotServer class)")
@Examples({"on load:",
        "\tbroadcast server \"lobby\""})
@Since("3.0")
public class ExprServer extends SimpleExpression<SpigotServer> {

    static {
        Skript.registerExpression(ExprServer.class, SpigotServer.class, ExpressionType.COMBINED,
            "server %string%");
    }
    
    Expression<String> name;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) expr[0];
        return true;
    }

    @Override
    protected SpigotServer[] get(Event e) {
        return new SpigotServer[] {  Skcrew.getInstance().getSocketClientListener().getServer(name.getSingle(e)) };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends SpigotServer> getReturnType() {
        return SpigotServer.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Server: " + name.toString(e,debug);
    }
}