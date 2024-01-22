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
import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import com.lotzy.skcrew.spigot.sockets.events.PlayerDisconnectedServerEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

@Name("Sockets - Player disconnects from proxied server event")
@Description("Called when player disconnects from proxied server")
@Examples({"on player disconnects from proxied server \"lobby\":",
        "\tbroadcast \"%player%\""})
@Since("3.0")
public class EvtPlayerDisconnectedServer extends SkriptEvent {
    
    static {
        Skript.registerEvent("PlayerConnectedServer", EvtPlayerDisconnectedServer.class, PlayerDisconnectedServerEvent.class, 
                "player (leave|disconnect[e])[(d|s)] [from] (proxied|network) server",  "player (leave|disconnect[e])[(d|s)] [from] (proxied|network) server %string%");

        EventValues.registerEventValue(PlayerDisconnectedServerEvent.class, OfflinePlayer.class, new Getter<OfflinePlayer, PlayerDisconnectedServerEvent>() {
            @Override
            public OfflinePlayer get(PlayerDisconnectedServerEvent event) {
                return event.getPlayer();
            }
        },0);
        EventValues.registerEventValue(PlayerDisconnectedServerEvent.class, SpigotServer.class, new Getter<SpigotServer, PlayerDisconnectedServerEvent>() {
            @Override
            public SpigotServer get(PlayerDisconnectedServerEvent event) {
                return event.getServer();
            }
        },0);
    }
    
    private Literal<String> serverName = null;
    
    @Override
    public boolean init(final Literal<?>[] args, final int matchedPattern, final ParseResult parser) {
        if (matchedPattern==1)
            serverName = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(final Event e) {
        PlayerDisconnectedServerEvent event = (PlayerDisconnectedServerEvent) e;
        if (serverName != null)
            return serverName.getSingle().equalsIgnoreCase(event.getServer().getName());
        return true;
    }

    @Override
    public String toString(final Event e, final boolean debug) {
        return "player disconnected from proxied server";
    }
}
