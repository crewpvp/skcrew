package com.lotzy.skcrew.world.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Chunk;
import org.bukkit.event.Event;

public class EffUnloadChunk extends Effect {
    static {
        Skript.registerEffect(EffUnloadChunk.class,
            "unload chunk %chunk%",
            "unload chunk %chunk% without save");
    }

    private Expression<Chunk> expr;
    private Boolean unsave;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        expr = (Expression<Chunk>) exprs[0];
        unsave = matchedPattern == 1;
        return true;
    }
    
    
    
    @Override
    public String toString(Event e, boolean debug) {
        return "Unloading chunk "+expr.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
        expr.getSingle(e).unload(unsave); 
    }
}
