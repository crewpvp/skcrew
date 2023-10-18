package com.lotzy.skcrew.spigot.sockets.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.lotzy.skcrew.spigot.sockets.events.ProxyConnectEvent;
import org.bukkit.event.Event;

@Name("Sockets - Proxy connect event")
@Description("Called when this server connect to socket server")
@Examples({"on connects proxy:",
        "\tbroadcast \"Now server connect to proxy\""})
@Since("3.0")
public class EvtProxyConnect extends SkriptEvent {
    
    static {
        Skript.registerEvent("ProxyConnect", EvtProxyConnect.class, ProxyConnectEvent.class, "connect(ed|s) [to] proxy [server]", "proxy connect");
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
        return "connected to proxy";
    }
}
