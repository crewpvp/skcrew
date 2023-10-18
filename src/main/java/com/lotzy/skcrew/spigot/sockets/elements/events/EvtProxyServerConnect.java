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
import com.lotzy.skcrew.spigot.sockets.events.ProxyServerConnectEvent;
import org.bukkit.event.Event;

@Name("Sockets - Server connected event")
@Description("Called when server connects to proxy server")
@Examples({"on server connects proxy:",
        "\tbroadcast \"%server%\""})
@Since("3.0")
public class EvtProxyServerConnect extends SkriptEvent {
    
    static {
        Skript.registerEvent("ProxyServerConnect", EvtProxyServerConnect.class, ProxyServerConnectEvent.class, "server connect(ed|s) [to] proxy [server]");

        EventValues.registerEventValue(ProxyServerConnectEvent.class, SpigotServer.class, new Getter<SpigotServer, ProxyServerConnectEvent>() {
            @Override
            public SpigotServer get(ProxyServerConnectEvent event) {
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
        return "server connected proxy";
    }
}
