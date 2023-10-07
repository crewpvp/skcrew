package com.lotzy.skcrew.spigot.sockets.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.sockets.SocketPacket;
import java.io.Serializable;
import org.bukkit.event.Event;

@Name("Sockets - Send signal")
@Description("send object to proxy server")
@Examples({"on load:",
        "\tsend signal \"start\" 25 to all online servers"})
@Since("1.6")
public class EffSendSignal extends Effect {

    static {
        Skript.registerEffect(EffSendSignal.class,
            "send signal %string% %object% to [server[s]] %strings%");
    }
    
    private Expression<String> key;
    private Expression<Object> value;
    private Expression<String> servers;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        key = (Expression<String>) expr[0];
        value = LiteralUtils.defendExpression(expr[1]);
        servers = (Expression<String>) expr[2];
        return LiteralUtils.canInitSafely(value);
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "send signal";
    }
 
    @Override
    protected void execute(Event e) {
        String k = this.key.getSingle(e);
        Object val = this.value.getSingle(e);
        for (String server : servers.getAll(e))
            Skcrew.getInstance().socketClient.sendPacket(SocketPacket.SignalPacket(server, k, val));
    }
}
