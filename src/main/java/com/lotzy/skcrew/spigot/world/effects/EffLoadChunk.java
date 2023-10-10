package com.lotzy.skcrew.spigot.world.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Chunk;
import org.bukkit.event.Event;

@Name("World - Load chunk")
@Description("Load specifed chunk")
@Examples({"on load:",
        "\tload (chunk at spawn point of world(\"world\")"})
@Since("1.0")
public class EffLoadChunk extends Effect {
    static {
        Skript.registerEffect(EffLoadChunk.class,
            "load %chunk%",
            "load %chunk% without gen[erate]");
    }

    private Expression<Chunk> expr;
    private boolean generate;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        expr = (Expression<Chunk>) exprs[0];
        generate = matchedPattern == 0;
        return true;
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "Loading chunk: "+expr.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
        expr.getSingle(e).load(generate); 
    }
}
