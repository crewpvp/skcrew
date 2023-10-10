package com.lotzy.skcrew.spigot.sockets.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.shared.sockets.data.SpigotPlayer;
import com.lotzy.skcrew.spigot.Skcrew;
import java.util.UUID;
import org.bukkit.event.Event;

@Name("Sockets - Network Player")
@Description("Return player by name (or string UUID) (com.lotzy.skcrew.shared.sockets.data.SpigotPlayer class)")
@Examples({"on load:",
        "\tbroadcast \"%network player \"Lotzy\"%\""})
@Since("3.0")
public class ExprNetworkPlayer extends SimpleExpression<SpigotPlayer> {

    static {
        Skript.registerExpression(ExprNetworkPlayer.class, SpigotPlayer.class, ExpressionType.COMBINED,"network[ ]player %string%");
    }
    
    Expression<String> name;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) expr[0];
        return true;
    }

    @Override
    protected SpigotPlayer[] get(Event e) {
        try {
            return new SpigotPlayer[] { Skcrew.getInstance().getSocketClientListener().getPlayer(UUID.fromString(name.getSingle(e))) }; 
        } catch (IllegalArgumentException ex) {
            return new SpigotPlayer[] { Skcrew.getInstance().getSocketClientListener().getPlayer(name.getSingle(e)) };
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends SpigotPlayer> getReturnType() {
        return SpigotPlayer.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Network player: " + name.toString(e,debug);
    }
}