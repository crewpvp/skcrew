package com.lotzy.skcrew.spigot.sockets.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.lotzy.skcrew.spigot.sockets.events.PlayerDisconnectedProxyEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

@Name("Sockets - Player disconnected proxy event")
@Description("Called when player disconnects to proxy server")
@Examples({"on player disconnects proxy:",
        "\tbroadcast \"%player%\""})
@Since("3.0")
public class EvtPlayerDisconnectedProxy extends SkriptEvent {
    
    static {
        Skript.registerEvent("PlayerDisconnectedProxy", EvtPlayerDisconnectedProxy.class, PlayerDisconnectedProxyEvent.class, "player (leave|disconnect)[(ed|s)] [from] proxy [server]");

        EventValues.registerEventValue(PlayerDisconnectedProxyEvent.class, OfflinePlayer.class, new Getter<OfflinePlayer, PlayerDisconnectedProxyEvent>() {
            @Override
            public OfflinePlayer get(PlayerDisconnectedProxyEvent event) {
                return event.getPlayer();
            }
        },0);
    }

    @Override
    public boolean init(final Literal<?>[] args, final int matchedPattern, final ParseResult parser) {
        return true;
    }

    @Override
    public boolean check(final Event e) {
        return true;
    }

    @Override
    public String toString(final Event e, final boolean debug) {
        return "player disconnected proxy";
    }
}
