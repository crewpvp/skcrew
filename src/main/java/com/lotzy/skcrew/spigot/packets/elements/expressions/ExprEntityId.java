package com.lotzy.skcrew.spigot.packets.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

@Name("Packets - Get entity id")
@Description("Get entity id")
@Examples({"set {_id} to entity id of last spawned entity"})
@Since("3.4")
public class ExprEntityId extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprEntityId.class, Integer.class, ExpressionType.SIMPLE, 
            "%entity%'s entity id",
            "entity id of %entity%");
    }
    
    Expression<Entity> entity;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        entity = (Expression<Entity>)exprs[0];
        return true;
    }

    @Override
    protected Integer[] get(Event event) {
        return new Integer[] { entity.getSingle(event).getEntityId() };   
    }
    
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "entity id";
    }
}