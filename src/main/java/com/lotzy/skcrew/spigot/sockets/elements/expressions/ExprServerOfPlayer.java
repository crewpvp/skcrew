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
import org.bukkit.event.Event;


@Name("Sockets - Server of network player")
@Description("Return server of network player (com.lotzy.skcrew.shared.sockets.data.SpigotServer class)")
@Examples({"on load:",
        "\tbroadcast server of network player \"Lotzy\""})
@Since("3.0")
public class ExprServerOfPlayer extends SimpleExpression<SpigotServer> {

    static {
        Skript.registerExpression(ExprServerOfPlayer.class, SpigotServer.class, ExpressionType.COMBINED,
            "server of %networkplayer%");
    }
    
    Expression<SpigotPlayer> player;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<SpigotPlayer>) expr[0];
        return true;
    }

    @Override
    protected SpigotServer[] get(Event e) {
        SpigotPlayer spigotPlayer = player.getSingle(e);
        if (spigotPlayer != null) 
            return new SpigotServer[] { spigotPlayer.getServer()};
        return new SpigotServer[0];
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
        return "Server of player";
    }
}