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
import com.lotzy.skcrew.spigot.sockets.events.PlayerConnectedServerEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

@Name("Sockets - Player connects proxied server event")
@Description("Called when player connects to proxied server")
@Examples({"on player connects proxied server \"lobby\":",
        "\tbroadcast \"%player%\""})
@Since("3.0")
public class EvtPlayerConnectedServer extends SkriptEvent {
    
    static {
        Skript.registerEvent("PlayerConnectedServer", EvtPlayerConnectedServer.class, PlayerConnectedServerEvent.class, 
                "player (join|connect)[(ed|s)] [to] (proxied|network) server",  "player (join|connect)[(ed|s)] [to] (proxied|network) server %string%");

        EventValues.registerEventValue(PlayerConnectedServerEvent.class, OfflinePlayer.class, new Getter<OfflinePlayer, PlayerConnectedServerEvent>() {
            @Override
            public OfflinePlayer get(PlayerConnectedServerEvent event) {
                return event.getPlayer();
            }
        },0);
        EventValues.registerEventValue(PlayerConnectedServerEvent.class, SpigotServer.class, new Getter<SpigotServer, PlayerConnectedServerEvent>() {
            @Override
            public SpigotServer get(PlayerConnectedServerEvent event) {
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
        PlayerConnectedServerEvent event = (PlayerConnectedServerEvent) e;
        if (serverName != null)
            return serverName.getSingle().equalsIgnoreCase(event.getServer().getName());
        return true;
    }

    @Override
    public String toString(final Event e, final boolean debug) {
        return "player connected to proxied server";
    }
}
