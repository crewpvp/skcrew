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
import com.lotzy.skcrew.shared.sockets.data.Signal;
import com.lotzy.skcrew.spigot.sockets.events.SignalEvent;
import org.bukkit.event.Event;

@Name("Sockets - Signal event")
@Description("Called when server got signal")
@Examples({"on signal with key \"stop\":",
        "\texecute console command \"stop\""})
@Since("3.0")
public class EvtSignal extends SkriptEvent {
    
    static {
        Skript.registerEvent("Signal", EvtSignal.class, SignalEvent.class, 
                "signal",  "signal (with key|keyed) %string%");

        EventValues.registerEventValue(SignalEvent.class, Signal.class, new Getter<Signal, SignalEvent>() {
            @Override
            public Signal get(SignalEvent event) {
                return event.getSignal();
            }
        },0);
    }
    
    private Literal<String> key = null;
    
    @Override
    public boolean init(final Literal<?>[] args, final int matchedPattern, final ParseResult parser) {
        if (matchedPattern==1)
            key = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(final Event e) {
       SignalEvent event = (SignalEvent) e;
        if (key != null)
            return key.getSingle().equalsIgnoreCase(event.getKey());
        return true;
    }

    @Override
    public String toString(final Event e, final boolean debug) {
        return "signal event";
    }
}
