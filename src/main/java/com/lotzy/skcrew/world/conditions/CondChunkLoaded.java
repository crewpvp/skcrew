package com.lotzy.skcrew.world.conditions;

import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Chunk;

public class CondChunkLoaded extends Condition {

    static {
        Skript.registerCondition(CondChunkLoaded.class,
            "%chunk% [is] load[ed]",
            "%chunk% (does|is)(n't| not) load[ed]");
    }

    private Expression<Chunk> expr;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        expr = (Expression<Chunk>) exprs[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        return isNegated() != expr.getSingle(e).isLoaded();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Chunk "+expr.toString(e, debug) + " is " + (isNegated() ? " unloaded" : " loaded");
    }

}