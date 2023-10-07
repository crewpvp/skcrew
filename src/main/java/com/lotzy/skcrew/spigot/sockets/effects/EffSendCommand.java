package com.lotzy.skcrew.spigot.sockets.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.sockets.SocketPacket;
import org.bukkit.event.Event;

@Name("Sockets - Send signal")
@Description("send command to proxy server")
@Examples({"on load:",
        "\tsend command \"sk reload test.sk\" to all online servers"})
@Since("1.6")
public class EffSendCommand extends Effect {

    static {
        Skript.registerEffect(EffSendCommand.class,
            "send command[s] %strings% to [server[s]] %strings%");
    }
    
    private Expression<String> command;
    private Expression<String> servers;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        command = (Expression<String>) expr[0];
        servers = (Expression<String>) expr[1];
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "send command";
    }
 
    @Override
    protected void execute(Event e) {
        String[] command = this.command.getAll(e);
        for (String server : servers.getAll(e))
            Skcrew.getInstance().socketClient.sendPacket(SocketPacket.CommandPacket(server, command));
    }
}
