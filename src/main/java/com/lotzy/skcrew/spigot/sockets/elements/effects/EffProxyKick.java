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
import com.lotzy.skcrew.shared.sockets.data.BasePlayer;
import com.lotzy.skcrew.shared.sockets.packets.PacketKickPlayer;
import com.lotzy.skcrew.spigot.Skcrew;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.bukkit.OfflinePlayer;
 
@Name("Sockets - Proxy kick")
@Description("Kick player from proxy server")
@Examples({"on load:",
        "\tkick \"Lotzy\" parsed as offline player from proxy"})
@Since("3.0")
public class EffProxyKick extends Effect {

    static {
        Skript.registerEffect(EffProxyKick.class, 
            "kick %offlineplayers% from proxy",
            "kick %offlineplayers% from proxy (by reason of|because [of]|on account of|due to) %string%");
    }
    
    private Expression<OfflinePlayer> players;
    private Expression<String> reason;
    boolean withReason;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        players = (Expression<OfflinePlayer>)expr[0];
        if (matchedPattern == 1) {
            reason = (Expression<String>)expr[1];
            withReason = true;
        }
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        if (!withReason) {
            return "Kick "+ players.toString(e, debug) + " from proxy";
        } else {
            return "Kick "+ players.toString(e, debug) + " from proxy due to " + reason.toString(e, debug);
        }
    }
 
    @Override
    protected void execute(Event e) {
        String reason = withReason ? this.reason.getSingle(e) : null;
        Skcrew.getInstance().getSocketClientListener().sendPacket(new PacketKickPlayer(reason,Arrays.asList(players.getAll(e))
            .stream().map(player -> new BasePlayer(player.getName(),player.getUniqueId())).collect(Collectors.toSet())));
    }
}
