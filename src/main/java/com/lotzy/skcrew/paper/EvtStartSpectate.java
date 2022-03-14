package com.lotzy.skcrew.paper;

import ch.njol.skript.Skript;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import javax.annotation.Nullable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class EvtStartSpectate extends SkriptEvent {
	static {
		Skript.registerEvent("StartSpectate", EvtStartSpectate.class, PlayerStartSpectatingEntityEvent.class, "start spectate", "start spectate of %entitydata%");
	
                EventValues.registerEventValue(PlayerStartSpectatingEntityEvent.class, Player.class, new Getter<Player, PlayerStartSpectatingEntityEvent>() {
                    @Override
                    public Player get(PlayerStartSpectatingEntityEvent event) {
                        return event.getPlayer();
                    }
                },0);
                EventValues.registerEventValue(PlayerStartSpectatingEntityEvent.class, Entity.class, new Getter<Entity, PlayerStartSpectatingEntityEvent>() {
                    @Override
                    public Entity get(PlayerStartSpectatingEntityEvent event) {
                        return event.getNewSpectatorTarget();
                    }
                },0);
	}
        
        
        
        @Nullable
	private Literal<EntityData> entity = null;
	
	@Override
	public boolean init(final Literal<?>[] args, final int matchedPattern, final ParseResult parser) {
            if (matchedPattern==1) {
                entity = (Literal<EntityData>) args[0];
            }
            return true;
	}
	
	@Override
	public boolean check(final Event e) {
            PlayerStartSpectatingEntityEvent evt = (PlayerStartSpectatingEntityEvent) e;
            if (entity != null) {
                Entity ent = evt.getCurrentSpectatorTarget();
                return EntityData.fromEntity(ent) == entity;
            }
            return true;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "start spectate" + entity == null ? "" : entity.toString(e, debug);
	}
	
}
