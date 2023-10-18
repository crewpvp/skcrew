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
import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import com.lotzy.skcrew.shared.sockets.packets.PacketSwitchPlayer;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.bukkit.OfflinePlayer;
 
@Name("Sockets - Switch player")
@Description("Switch player to another server")
@Examples({"on load:",
        "\tswitch (players from all servers) to (server \"Lobby\")"})
@Since("3.0")
public class EffProxySwitch extends Effect {

    static {
        Skript.registerEffect(EffProxySwitch.class, "switch %offlineplayers% to %server%");
    }
    
    private Expression<OfflinePlayer> players;
    private Expression<SpigotServer> server;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        players = (Expression<OfflinePlayer>)expr[0];
        server = (Expression<SpigotServer>)expr[1];
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "Switch "+ players.toString(e, debug) + " to " + server.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
        Skcrew.getInstance().getSocketClientListener().sendPacket(new PacketSwitchPlayer(server.getSingle(e).toBaseServer(),
            Arrays.asList(players.getAll(e)).stream().map(player -> new BasePlayer(player.getName(),player.getUniqueId())).collect(Collectors.toSet())));
    }
}
