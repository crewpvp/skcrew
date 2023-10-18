package com.lotzy.skcrew.spigot.sockets.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.lotzy.skcrew.spigot.sockets.events.ProxyReconnectEvent;
import org.bukkit.event.Event;

@Name("Sockets - Proxy reconnect event")
@Description("Called when this server try reconnect to socket server (but not actually connect)")
@Examples({"on reconnects proxy:",
        "\tbroadcast \"Reconnecting\""})
@Since("3.0")
public class EvtProxyReconnect extends SkriptEvent {
    
    static {
        Skript.registerEvent("ProxyReconnect", EvtProxyReconnect.class, ProxyReconnectEvent.class, "reconnect(ing|s) [to] proxy [server]", "proxy reconnect");
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
        return "try reconnected to proxy";
    }
}
