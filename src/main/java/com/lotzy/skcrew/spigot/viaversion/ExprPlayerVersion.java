package com.lotzy.skcrew.spigot.viaversion;

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
    protected String[] get( Event e) {
        int ver = Via.getAPI().getPlayerVersion(player.getSingle(e));
        if (ver > 765)  return new String[] {"1.20.4+"};
        if (ver >= 765) return new String[] {"1.20.4"};
        if (ver >= 764) return new String[] {"1.20.2"};
        if (ver >= 763) return new String[] {"1.20.1"};
        if (ver >= 762) return new String[] {"1.19.4"};
        if (ver >= 761) return new String[] {"1.19.3"};
        if (ver >= 760) return new String[] {"1.19.2"};
        if (ver >= 759) return new String[] {"1.19.0"};
        if (ver >= 758) return new String[] {"1.18.2"};
        if (ver >= 757) return new String[] {"1.18.1"};
        if (ver >= 756) return new String[] {"1.17.1"};
        if (ver >= 755) return new String[] {"1.17.0"};
        if (ver >= 754) return new String[] {"1.16.5"};
        if (ver >= 752) return new String[] {"1.16.3"};
        if (ver >= 738) return new String[] {"1.16.2"};
        if (ver >= 736) return new String[] {"1.16.1"};
        if (ver >= 701) return new String[] {"1.16.0"};
        if (ver >= 576) return new String[] {"1.15.2"};
        if (ver >= 574) return new String[] {"1.15.1"};
        if (ver >= 550) return new String[] {"1.15.0"}; 
        if (ver >= 550) return new String[] {"1.15.0"};
        if (ver >= 491) return new String[] {"1.14.4"};
        if (ver >= 486) return new String[] {"1.14.3"};
        if (ver >= 481) return new String[] {"1.14.2"};
        if (ver >= 478) return new String[] {"1.14.1"};
        if (ver >= 441) return new String[] {"1.14.0"};
        if (ver >= 402) return new String[] {"1.13.2"};
        if (ver >= 394) return new String[] {"1.13.1"};
        if (ver >= 341) return new String[] {"1.13.0"};
        if (ver >= 339) return new String[] {"1.12.2"};
        if (ver >= 336) return new String[] {"1.12.1"};
        if (ver >= 317) return new String[] {"1.12.0"};
        if (ver >= 316) return new String[] {"1.11.2"};
        if (ver >= 301) return new String[] {"1.11.0"};
        if (ver >= 210) return new String[] {"1.10.2"};
        if (ver >= 201) return new String[] {"1.10.0"};
        if (ver >= 110) return new String[] {"1.9.4"};
        if (ver >= 109) return new String[] {"1.9.3"};
        if (ver >= 108) return new String[] {"1.9.2"};
        if (ver >= 48)  return new String[] {"1.9.1"};
        if (ver >= 47)  return new String[] {"1.8.9"};
        if (ver >= 6)   return new String[] {"1.8.0"};
        return new String[] {"1.7.2"};
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
        return "Version of player: " + player.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern,Kleenean isDelayed, SkriptParser. ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        return true;
    }
}
