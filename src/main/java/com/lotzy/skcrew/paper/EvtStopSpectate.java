package com.lotzy.skcrew.paper;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import javax.annotation.Nullable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Paper - Stop spectate event")
@Description("Called when player stop spectate entity")
@Examples({"on stop spectate of player:",
        "\tbroadcast \"%player% %event-entity%\""})
@Since("1.0")
public class EvtStopSpectate extends SkriptEvent {
	static {
            Skript.registerEvent("StopSpectate", EvtStopSpectate.class, PlayerStopSpectatingEntityEvent.class, "stop spectate", "stop spectate of %entitydata%");
	
            EventValues.registerEventValue(PlayerStopSpectatingEntityEvent.class, Player.class, new Getter<Player, PlayerStopSpectatingEntityEvent>() {
                @Override
                public Player get(PlayerStopSpectatingEntityEvent event) {
                    return event.getPlayer();
                }
            },0);
            EventValues.registerEventValue(PlayerStopSpectatingEntityEvent.class, Entity.class, new Getter<Entity, PlayerStopSpectatingEntityEvent>() {
                @Override
                public Entity get(PlayerStopSpectatingEntityEvent event) {
                    return event.getSpectatorTarget();
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
            PlayerStopSpectatingEntityEvent evt = (PlayerStopSpectatingEntityEvent) e;
            if (entity != null) {
                Entity ent = evt.getSpectatorTarget();
                return EntityData.fromEntity(ent) == entity;
            }
            return true;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "stop spectate" + entity == null ? "" : entity.toString(e, debug);
	}
	
}
