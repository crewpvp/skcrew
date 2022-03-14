package com.lotzy.skcrew.world.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import javax.annotation.Nullable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;

public class ExprWorldBorderCenter extends SimplePropertyExpression<World, Location> {

        static {
            register(ExprWorldBorderCenter.class, Location.class,
                "[world] border center", "world"
            );
        }
        
	@Override
	public Class<? extends Location> getReturnType() {
		return Location.class;
	}

        
        
	@Override
	protected String getPropertyName() {
		return "world border center";
	}

	@Override
	@Nullable
	public Location convert(World world) {
		return world.getWorldBorder().getCenter();
	}

	@Override
	public Class[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.SET) {
			return new Class[] { Location.class };
                }
		return null;
	}

	@Override
	public void change(Event e, Object[] delta, ChangeMode mode) {
		if (delta[0] == null)
			return;
		Location location = (Location) delta[0];
		for (World world : getExpr().getArray(e))
			world.getWorldBorder().setCenter(location);
	}

}
