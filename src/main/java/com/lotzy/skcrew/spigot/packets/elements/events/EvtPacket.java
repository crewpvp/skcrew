package com.lotzy.skcrew.spigot.packets.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.lotzy.skcrew.spigot.packets.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Packet - Packet event")
@Description("Called when server send packet to player or receive packet from player")
@Examples({"on packet PacketPlayOutOpenSignEditor:",
        "\tbroadcast \"%event-packet%\""})
@Since("3.4")
public class EvtPacket extends SkriptEvent {
    
    static {
        Skript.registerEvent("Packet", EvtPacket.class, PacketEvent.class, 
                "[any] packet",  "packet [named] <(.+)>");

        EventValues.registerEventValue(PacketEvent.class, Player.class, new Getter<Player, PacketEvent>() {
            @Override
            public Player get(PacketEvent event) {
                return event.getPlayer();
            }
        },0);
    }
    
    @Override
    public boolean canExecuteAsynchronously() {
	return true;
    }
    
    private String regex = null;
    
    @Override
    public boolean init(final Literal<?>[] args, final int matchedPattern, final SkriptParser.ParseResult parser) {
        if (matchedPattern==1)
            regex = parser.regexes.get(0).group();
        return true;
    }

    @Override
    public boolean check(final Event event) {
        if (regex != null)
            return regex.equalsIgnoreCase(((PacketEvent)event).getPacket().getClass().getSimpleName());
        return true;
    }

    @Override
    public String toString(final Event e, final boolean debug) {
        return "Packet event";
    }
}

