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
import com.lotzy.skcrew.spigot.sockets.events.ProxyServerDisconnectEvent;
import org.bukkit.event.Event;

@Name("Sockets - Server disconnected event")
@Description("Called when server disconnects from proxy server")
@Examples({"on server disconnects from proxy:",
        "\tbroadcast \"%server%\""})
@Since("3.0")
public class EvtProxyServerDisconnect extends SkriptEvent {
    
    static {
        Skript.registerEvent("ProxyServerDisconnect", EvtProxyServerDisconnect.class, ProxyServerDisconnectEvent.class, "server disconnect(ed|s) [from] proxy [server]");

        EventValues.registerEventValue(ProxyServerDisconnectEvent.class, SpigotServer.class, new Getter<SpigotServer, ProxyServerDisconnectEvent>() {
            @Override
            public SpigotServer get(ProxyServerDisconnectEvent event) {
                return event.getServer();
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
        return "server disconnected proxy";
    }
}
