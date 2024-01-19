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
import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import java.util.Arrays;

@Name("Servers - Is server connected")
@Description("Check if server is actually connected")
@Examples({"on load:",
        "\tif server \"lobby\" is connected:",
        "\t\tbroadcast \"yes\""})
@Since("3.0")
public class CondServerIsOnline extends Condition {

    static {
        Skript.registerCondition(CondServerIsOnline.class,
            "%servers% is (online|connected)","%servers% is(n't| not) (online|connected)");
    }

    private Expression<SpigotServer> servers;

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        servers = (Expression<SpigotServer>) expr[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {  
        return isNegated() != ((int)(Arrays.asList(servers.getAll(e)).stream().filter(server -> server.isConnected()).count()) > 0);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Servers is online: "+servers.toString(e, debug);
    }
}