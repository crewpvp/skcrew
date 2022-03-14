package com.lotzy.skcrew.other;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import javax.annotation.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class EvtInventoryDrag extends SkriptEvent {
	static {
		Skript.registerEvent("InventoryDrag", EvtInventoryDrag.class, InventoryDragEvent.class, "inventory drag");
	
                EventValues.registerEventValue(InventoryDragEvent.class, Player.class, new Getter<Player, InventoryDragEvent>() {
                    @Override
                    public Player get(InventoryDragEvent event) {
                        return (Player)event.getWhoClicked();
                    }
                },0);
                EventValues.registerEventValue(InventoryDragEvent.class, Inventory.class, new Getter<Inventory, InventoryDragEvent>() {
                    @Override
                    public Inventory get(InventoryDragEvent event) {
                        return event.getInventory();
                    }
                },0);
	}
        
	@Override
	public boolean init(final Literal<?>[] args, final int matchedPattern, final ParseResult parser) {
            return true;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "inventory drag";
	}

        @Override
        public boolean check(Event e) {
            return true;
            
        }
	
}
