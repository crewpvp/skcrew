package com.lotzy.skcrew.spigot.floodgate;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.geysermc.floodgate.api.FloodgateApi;

@Name("Forms - Is floodgate player")
@Description("Check if player from bedrock edition")
@RequiredPlugins("Floodgate")
@Examples({"if player is from floodgate:",
        "\tbroadcast \"%player% from floodgate\""})
@Since("1.0")
public class CondFloodgatePlayer extends Condition {

    static {
        Skript.registerCondition(CondFloodgatePlayer.class, "%player% [(is|does)] from floodgate",
            "%player% [(is|does)](n't| not) from floodgate");
    }

    private Expression<Player> player;

    @Override
    public String toString( Event e, boolean debug) {
        return "is player from Floodgate: " + player.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern,Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check( Event e) {
        return isNegated() != (FloodgateApi.getInstance().isFloodgatePlayer(player.getSingle(e).getUniqueId()));
    }
}
