package com.lotzy.skcrew.spigot.sockets.expressions;

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
import com.lotzy.sockets.ServerInfo;
import java.util.ArrayList;
import org.bukkit.event.Event;

@Name("Sockets - All servers")
@Description("Get name of this server from velocity proxy")
@Examples({"on load:",
        "\tbroadcast server's name"})
@Since("1.6")
public class ExprAllServers extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprAllServers.class, String.class, ExpressionType.SIMPLE,
                "[all] servers", "[all] online servers"
        );
    }

    Boolean online;
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        online = matchedPattern == 1;
        return true;
    }

    @Override
    protected String[] get(Event e) {
        ArrayList<String> servers = new ArrayList();
        for(ServerInfo srv : Skcrew.getInstance().socketClient.servers)
            if (!online || srv.isOnline())
                servers.add(srv.getName());
        return servers.toArray(new String[0]);
    }

    
    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "all servers";
    }
}