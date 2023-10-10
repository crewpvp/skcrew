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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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

    public static Entity lastSpawned = null;
    public static Expression<Player> players;
    private Expression<?> drops;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        drops = exprs[0];
        players = (Expression<Player>) exprs[1];
        return true;
    }

	@Override
    public void execute(Event event) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (Object object : this.drops.getArray(event)) {
            if (object instanceof Experience) {
                for (Player player : this.players.getArray(event))
                    player.giveExp(((Experience) object).getXP());
            } else {
                itemStacks.add(((ItemType)object).getRandom());
            }
        }

        ItemStack[] itemStacksArray = itemStacks.toArray(itemStacks.toArray(new ItemStack[0]));

        for (Player player : this.players.getArray(event)) {
            HashMap<Integer, ItemStack> leftOvers = player.getInventory().addItem(itemStacksArray);
            if (!leftOvers.isEmpty()) {
                Location location = player.getLocation();
                World world = location.getWorld();
                leftOvers.values().forEach(leftOver -> lastSpawned = world.dropItem(location, leftOver));
            }
        }
    }
        
    @Override
    public String toString(Event event, boolean debug) {
        return "give or drop " + drops.toString(event, debug) + " to " + players.toString(event, debug);
    }
}

