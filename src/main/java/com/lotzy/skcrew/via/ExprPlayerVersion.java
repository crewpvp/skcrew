package com.lotzy.skcrew.via;

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

@Name("Via - Player version")
@Description("Get version of player")
@RequiredPlugins("ViaVersion")
@Examples("\tbroadcast version of player")
@Since("1.2")
public class ExprPlayerVersion extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprPlayerVersion.class, String.class, ExpressionType.COMBINED,
                "[the] version of %player%",
                "%player%'s version");
    }

    private Expression<Player> player;

    @Override
    protected String[] get(@NotNull Event e) {
        int ver = Via.getAPI().getPlayerVersion(player.getSingle(e));
        if(ver < 49) {
            return new String[] {"1.8.9"};
        } else if (ver < 201) {
            return new String[] {"1.9.4"};
        } else if (ver < 301) {
            return new String[] {"1.10.2"};
        } else if (ver < 317) {
            return new String[] {"1.11.2"};
        } else if (ver < 341) {
            return new String[] {"1.12.2"};
        } else if (ver < 440) {
            return new String[] {"1.13.2"};
        } else if (ver < 500) {
            return new String[] {"1.14.4"};
        } else if (ver < 600) {
            return new String[] {"1.15.2"};
        } else if (ver < 755) {
            return new String[] {"1.16.5"};
        } else if (ver < 757) {
            return new String[] {"1.17.1"};
        } else if (ver < 758) {
            return new String[] {"1.18.2"};
        } else {
            return new String[] {"1.19+"};
        }
     
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "Get version of player " + player.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        return true;
    }
}
