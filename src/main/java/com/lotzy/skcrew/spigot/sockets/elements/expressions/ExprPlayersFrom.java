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
import com.lotzy.skcrew.shared.sockets.data.SpigotPlayer;
import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import com.lotzy.skcrew.spigot.Skcrew;
import org.bukkit.event.Event;


@Name("Sockets - Players from server")
@Description("Return players from servers (com.lotzy.skcrew.shared.sockets.data.SpigotPlayer class)")
@Examples({"on load:",
        "\tbroadcast players from server \"lobby\""})
@Since("3.0")
public class ExprPlayersFrom extends SimpleExpression<SpigotPlayer> {

    static {
        Skript.registerExpression(ExprPlayersFrom.class, SpigotPlayer.class, ExpressionType.COMBINED,
            "players (from|of|on) %servers%", "%servers%'s players");
    }
    
    Expression<SpigotServer> servers;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        servers = (Expression<SpigotServer>) expr[0];
        return true;
    }

    @Override
    protected SpigotPlayer[] get(Event e) {
        return Skcrew.getInstance().getSocketClientListener().getPlayers(servers.getSingle(e)).toArray(new SpigotPlayer[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends SpigotPlayer> getReturnType() {
        return SpigotPlayer.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Players from servers: " + servers.toString(e,debug);
    }
}
