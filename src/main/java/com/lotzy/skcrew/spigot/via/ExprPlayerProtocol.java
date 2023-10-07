package com.lotzy.skcrew.spigot.via;

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
import com.viaversion.viaversion.api.Via;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Via - Player protocol")
@Description("Get protocol of player")
@RequiredPlugins("ViaVersion")
@Examples("\tbroadcast \"%protocol of player%\"")
@Since("1.2")
public class ExprPlayerProtocol extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprPlayerProtocol.class, Integer.class, ExpressionType.COMBINED,
                "[the] protocol of %player%",
                "%player%'s protocol");
    }

    private Expression<Player> player;

    @Override
    protected Integer[] get(@NotNull Event e) {
        return new Integer[]{Via.getAPI().getPlayerVersion(player.getSingle(e))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "Get protocol of player " + player.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        return true;
    }
}
