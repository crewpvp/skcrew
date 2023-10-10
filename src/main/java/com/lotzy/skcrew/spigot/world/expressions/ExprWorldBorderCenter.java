package com.lotzy.skcrew.spigot.world.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;

@Name("World - Center of world border")
@Description("Get or set center location of world border")
@Examples({"on load:",
        "\tset border center of world(\"world\") to spawn point of world(\"world\")"})
@Since("1.0")
public class ExprWorldBorderCenter extends SimplePropertyExpression<World, Location> {

    static {
        register(ExprWorldBorderCenter.class, Location.class,"[world] border center", "world");
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    protected String getPropertyName() {
        return "World border center";
    }

    @Override
    public Location convert(World world) {
        return world.getWorldBorder().getCenter();
    }

    @Override
    public Class[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) return new Class[] { Location.class };
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, ChangeMode mode) {
        if (delta[0] == null) return;
        Location location = (Location) delta[0];
        for (World world : getExpr().getArray(e))
            world.getWorldBorder().setCenter(location);
    }
}
