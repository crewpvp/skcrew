package com.lotzy.skcrew.world.expressions;

import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Chunk;
import org.bukkit.World;

public class ExprWorldChunk extends SimpleExpression<Chunk> {

    static {
        Skript.registerExpression(ExprWorldChunk.class, Chunk.class, ExpressionType.COMBINED, "chunk [at] %number%,[ ]%number% (in|at) %world%");
    }
    Expression<Number> expr1;
    Expression<Number> expr2;
    Expression<World> expr3;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        expr1 = (Expression<Number>) exprs[0];
        expr2 = (Expression<Number>) exprs[1];
        expr3 = (Expression<World>) exprs[2];
        return true;
    }

    @Override
    protected Chunk[] get(Event e) {
        return new Chunk[] { expr3.getSingle(e).getChunkAt(expr1.getSingle(e).intValue(),expr2.getSingle(e).intValue()) };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Chunk> getReturnType() {
        return Chunk.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "chunk "+expr1.toString(e,debug)+", "+expr2.toString(e,debug)+
                " in world "+expr3.toString(e,debug);
    }

}