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


@Name("Sockets - Servers")
@Description("Return all servers (or online servers) (com.lotzy.skcrew.shared.sockets.data.SpigotServer class)")
@Examples({"on load:",
        "\tbroadcast \"%all servers%\""})
@Since("3.0")
public class ExprServers extends SimpleExpression<SpigotServer> {

    static {
        Skript.registerExpression(ExprServers.class, SpigotServer.class, ExpressionType.COMBINED,
            "[all] servers", "[all] online servers");
    }
    
    boolean online;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        online = matchedPattern == 1;
        return true;
    }

    @Override
    protected SpigotServer[] get(Event e) {
        if (online) 
            return Skcrew.getInstance().getSocketClientListener().getOnlineServers().toArray(new SpigotServer[0]);
        return Skcrew.getInstance().getSocketClientListener().getServers().toArray(new SpigotServer[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends SpigotServer> getReturnType() {
        return SpigotServer.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        if (online)
            return "All onlnie servers";
        return "All servers";
    }
}