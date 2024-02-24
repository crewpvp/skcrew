package com.lotzy.skcrew.spigot.packets.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.packets.PacketReflection;
import java.util.Arrays;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Packets - Send packet")
@Description("Sends created packets to players")
@Examples({"send packet {_packet} to all players"})
@Since("3.4")
public class EffSendPacket extends Effect {

    static {
        Skript.registerEffect(EffSendPacket.class, 
            "send packet %packets% to %players%",
            "send packet %packets% to %players% without [(trigger|call)[ing]] [the] event",
            "send packet %packets% without [(trigger|call)[ing]] [the] event to %players%");
    }
    
    private Expression<Object> packets;
    private Expression<Player> players;
    private boolean withoutCalling;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        packets = (Expression<Object>)expr[0];
        players = (Expression<Player>)expr[1];
        withoutCalling = matchedPattern > 0;
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "send packets";
    }
 
    @Override
    protected void execute(Event e) {
        if (withoutCalling) {
            PacketReflection.sendPacketWithoutEvent(Arrays.asList(players.getArray(e)), Arrays.asList(packets.getArray(e)));
        } else {
            PacketReflection.sendPacket(Arrays.asList(players.getArray(e)), Arrays.asList(packets.getArray(e)));
        }
    }
}
