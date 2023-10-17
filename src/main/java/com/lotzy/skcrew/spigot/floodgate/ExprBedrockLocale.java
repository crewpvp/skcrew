package com.lotzy.skcrew.spigot.floodgate;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

@Name("Forms - Bedrock Locale")
@Description("Get locale of bedrock player")
@RequiredPlugins("Floodgate")
@Examples("\tbroadcast be locale of player")
@Since("1.0")
public class ExprBedrockLocale extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprBedrockLocale.class, String.class, ExpressionType.COMBINED,
            "[the] be[[drock] [edition]] (locale|language) of [the] [floodgate] %player%",
            "%player%'s be[[drock] [edition]] (locale|language) [of [the] floodgate]");
    }

    private Expression<Player> player;

    @Override
    protected String[] get( Event e) {
        Player bukkitPlayer = player.getSingle(e);
        if (bukkitPlayer != null) {
            FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(bukkitPlayer.getUniqueId());
            if (floodgatePlayer != null) {
                return new String[] {floodgatePlayer.getLanguageCode()};
            }
        }
        return new String[] {"Java"};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "Get Floodgate player's language: " + player.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern,Kleenean isDelayed, SkriptParser. ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        return true;
    }
}
