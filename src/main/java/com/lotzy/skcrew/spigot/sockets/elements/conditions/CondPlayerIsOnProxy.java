package com.lotzy.skcrew.spigot.sockets.elements.conditions;

import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.skcrew.spigot.sockets.SocketClientListener;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.OfflinePlayer;

@Name("Servers - Is player connected to proxy")
@Description("Check if player is actually connected to proxy")
@Examples({"on load:",
        "\tif (\"Lotzy\" parsed as offline player) is connected to proxy:",
        "\t\tbroadcast \"yes\""})
@Since("3.0")
public class CondPlayerIsOnProxy extends Condition {

    static {
        Skript.registerCondition(CondPlayerIsOnProxy.class,
            "%offlineplayers% is ([online ]on|connected to) proxy","%offlineplayers% (does|is)(n't| not) ([online ]on|connected to) proxy");
    }

    private Expression<OfflinePlayer> players;

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        players = (Expression<OfflinePlayer>) expr[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {  
        SocketClientListener client = Skcrew.getInstance().getSocketClientListener();
        Set<BasePlayer> basePlayers = Arrays.asList(players.getAll(e)).stream().map(player -> new BasePlayer(player.getName(),player.getUniqueId())).collect(Collectors.toSet());
        return isNegated() != ((int)(basePlayers.stream().filter(player -> client.getPlayer(player) != null).count()) == basePlayers.size());
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Player is online on proxy: "+players.toString(e, debug);
    }
}