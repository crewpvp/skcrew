package com.lotzy.skcrew.world.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import javax.annotation.Nullable;
import org.bukkit.Chunk;
import org.bukkit.event.Event;

public class ExprForceLoadedChunk extends SimplePropertyExpression<Chunk, Boolean> {

        static {
            register(ExprForceLoadedChunk.class, Boolean.class,
                "force load[ed]", "chunk"
            );
        }
        
	@Override
	public Class<? extends Boolean> getReturnType() {
		return Boolean.class;
	}

	@Override
	protected String getPropertyName() {
		return "chunk force load";
	}

	@Override
	@Nullable
	public Boolean convert(Chunk chunk) {
		return chunk.isForceLoaded();
	}

	@Override
	public Class[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.SET) {
			return new Class[] { Boolean.class };
                }
		return null;
	}

	@Override
	public void change(Event e, Object[] delta, ChangeMode mode) {
		if (delta[0] == null)
			return;
		Boolean loaded = (Boolean) delta[0];
		for (Chunk chunk : getExpr().getArray(e))
			chunk.setForceLoaded(loaded);;
	}

}
