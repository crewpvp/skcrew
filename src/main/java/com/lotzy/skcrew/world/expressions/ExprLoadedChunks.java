package com.lotzy.skcrew.world.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.Event;

public class ExprLoadedChunks extends SimpleExpression<Chunk> {

    static {
        Skript.registerExpression(ExprLoadedChunks.class, Chunk.class, ExpressionType.COMBINED,
            "[all [[of] the]] loaded chunks (in|at|of) %world%", "%world%'s [all [[of] the]] loaded chunks"
        );
    }

    private Expression<World> world;
    
    @Override
    protected Chunk[] get(Event e) {
        return world.getSingle(e).getLoadedChunks();
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Chunk> getReturnType() {
        return Chunk.class;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        world = (Expression<World>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "loaded chunks of world "+world.toString(e,debug);
    }

    
    
}
