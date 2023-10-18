package com.lotzy.skcrew.spigot.sockets.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.lotzy.skcrew.spigot.sockets.events.ProxyDisconnectEvent;
import org.bukkit.event.Event;

@Name("Sockets - Proxy disconnect event")
@Description("Called when this server disconnects from socket server")
@Examples({"on disconnects proxy:",
        "\tbroadcast \"Now server disconnected from proxy\""})
@Since("3.0")
public class EvtProxyDisconnect extends SkriptEvent {
    
    static {
        Skript.registerEvent("ProxyDisconnect", EvtProxyDisconnect.class, ProxyDisconnectEvent.class, "disconnect(ed|s) [from] proxy [server]", "proxy disconnect");
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
        return "disconnected from proxy";
    }
}
