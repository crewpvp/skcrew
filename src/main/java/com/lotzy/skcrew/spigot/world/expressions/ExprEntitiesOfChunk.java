package com.lotzy.skcrew.spigot.world.expressions;

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
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

@Name("World - Entities in chunk")
@Description("Get all entities in specifed chunk")
@Examples({"command /mobs:",
        "\ttrigger:",
        "\t\tsend \"%amount of entities in player's chunk%\""})
@Since("1.0")
public class ExprEntitiesOfChunk extends SimpleExpression<Entity> {

    static {
        Skript.registerExpression(ExprEntitiesOfChunk.class, Entity.class, ExpressionType.COMBINED,
            "entit(y|ies) (of|in) %chunk%", "%chunk%'s entit(y|ies)"
        );
    }
    
    private Expression<Chunk> chunk;
    
    @Override
    protected Entity[] get(Event e) {
        return chunk.getSingle(e).getEntities();
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Entity> getReturnType() {
        return Entity.class;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        chunk = (Expression<Chunk>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Entities in chunk: "+chunk.toString(e,debug);
    } 
}
