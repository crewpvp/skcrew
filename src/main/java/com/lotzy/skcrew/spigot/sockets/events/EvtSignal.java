package com.lotzy.skcrew.spigot.sockets.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.lotzy.skcrew.spigot.sockets.SignalEvent;
import javax.annotation.Nullable;
import org.bukkit.event.Event;

@Name("Sockets - signal event")
@Description("Run when server get signal from proxy")
@Examples({"on signal:",
        "\tbroadcast \"%signal key% %signal value%\""})
@Since("1.6")
public class EvtSignal extends SkriptEvent {
	static {
		Skript.registerEvent("Signal", EvtSignal.class, SignalEvent.class, "signal [%-string%]");
	}
        
       	@Nullable
	private Literal<String> key;
        
	@Override
	public boolean init(final Literal<?>[] args, final int matchedPattern, final ParseResult parser) {
            key = (Literal<String>) args[0];
            return true;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "signal event";
	}

        @Override
        public boolean check(Event e) {
            if (key!=null)
                if (!((SignalEvent)e).getKey().equals(this.key.getSingle()))
                    return false;
            return true;
            
        }
	
}
