package com.lotzy.skcrew.spigot.sockets.elements.effects;

import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.shared.sockets.data.Signal;
import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import com.lotzy.skcrew.shared.sockets.packets.PacketIncomingSignal;
import com.lotzy.skcrew.spigot.Skcrew;
import java.util.Arrays;
import java.util.stream.Collectors;
 
@Name("Sockets - Send signal")
@Description("Send signal to server")
@Examples({"on load:",
        "\tsend signal (Signal with key \"zaloopa\" with data 1) to server \"Lobby\""})
@Since("3.0")
public class EffSendSignal extends Effect {

    static {
        Skript.registerEffect(EffSendSignal.class, "send signal %signals% to %servers%");
    }
    
    private Expression<Signal> signals;
    private Expression<SpigotServer> servers;
   
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        signals = (Expression<Signal>) expr[0];
        servers = (Expression<SpigotServer>) expr[1];
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "Send signals "+ signals.toString(e, debug) + " to " + servers.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
        Skcrew.getInstance().getSocketClientListener()
                .sendPacket(new PacketIncomingSignal(Arrays.asList(servers.getAll(e))
                        .stream().map(server -> server.toBaseServer()).collect(Collectors.toSet()),Arrays.asList(signals.getAll(e))));
    }
}
