package com.lotzy.skcrew.spigot.other;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Experience;
import ch.njol.util.Kleenean;
import javax.annotation.Nullable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Name("Other - Give or drop")
@Description("Give exp or items to player if he has space, else drop items near")
@Examples({"command /drop:",
        "\ttrigger:",
        "\t\tgive or drop (999 of dirt, 1234 of stone) to player"})
@Since("1.0")

public class EffGiveOrDrop extends Effect {

	static {
		Skript.registerEffect(EffGiveOrDrop.class, "give or drop %itemtypes/experiences% to %players%");
	}

    @Nullable
	public static Entity lastSpawned = null;
        
	@Nullable
	public static Expression<Player> players;

	@SuppressWarnings("NotNullFieldNotInitialized")
	private Expression<?> drops;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		drops = exprs[0];
		players = (Expression<Player>) exprs[1];
		return true;
	}

	@Override
	public void execute(Event e) {
            Object[] os = drops.getArray(e);
            for (Player p : players.getArray(e)) {
                Inventory inv = p.getInventory();
                Location loc = p.getLocation().clone().subtract(0.5,0.5,0.5);
                for (Object o : os) {
                    if (o instanceof Experience) {
                        p.giveExp(((Experience) o).getXP());
                    } else {
                        if (o instanceof ItemStack)
                            o = new ItemType((ItemStack) o);
                        for (ItemStack is : ((ItemType) o).getItem().getAll()) {
                            if (is.getType() != Material.AIR && is.getAmount() > 0) {
                                int maxStack = is.getMaxStackSize();
                                int amount = is.getAmount();
                                int mod = amount%maxStack;
                                int div = (int)(amount/maxStack);
                                for(int i = 0; i<div;i++) {
                                    is.setAmount(maxStack);
                                    if (inv.firstEmpty()>=0) {
                                        inv.addItem(is);
                                    } else {
                                        lastSpawned = p.getWorld().dropItemNaturally(loc, is);
                                    }
                                }
                                if(mod>0) {
                                    is.setAmount(mod);
                                    if (inv.firstEmpty()>=0) {
                                        inv.addItem(is);
                                    } else {
                                        lastSpawned = p.getWorld().dropItemNaturally(loc, is);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "give or drop " + drops.toString(e, debug) + " " + players.toString(e, debug);
	}

}

