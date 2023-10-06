package com.lotzy.skcrew.sockets.expressions;

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
import com.lotzy.skcrew.Skcrew;
import com.lotzy.sockets.ServerInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

@Name("Sockets - Players on server")
@Description("Get players from velocity proxy")
@Examples({"on load:",
        "\tbroadcast players from all servers"})
@Since("1.6")
public class ExprPlayersOnServer extends SimpleExpression<OfflinePlayer> {

    static {
        Skript.registerExpression(ExprPlayersOnServer.class, OfflinePlayer.class, ExpressionType.COMBINED,
                "player[s] (from|on) %strings%"
        );
    }

    Expression<String> servers;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        servers = (Expression<String>)expr[0];
        return true;
    }

    @Override
    protected OfflinePlayer[] get(Event e) {
        ArrayList<OfflinePlayer> players = new ArrayList();
        List<String> srvs = Arrays.asList(servers.getAll(e));
        for(ServerInfo srv : Skcrew.getInstance().socketClient.servers)
            if (srv.isOnline() && srvs.contains(srv.getName()))
                for(String p : srv.getPlayers())
                    players.add(Bukkit.getOfflinePlayer(p));
        return players.toArray(new OfflinePlayer[0]);
    }

    
    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends OfflinePlayer> getReturnType() {
        return OfflinePlayer.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "players from server";
    }
}