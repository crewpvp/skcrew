package com.lotzy.skcrew.world.expressions;

import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ExprMainWorld extends SimpleExpression<World> {

    static {
        Skript.registerExpression(ExprMainWorld.class, World.class, ExpressionType.SIMPLE, "main[ ]world");
    }
    
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    protected World[] get(Event e) {
        return new World[] { Bukkit.getServer().getWorlds().get(0) };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends World> getReturnType() {
        return World.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Main world";
    }

}