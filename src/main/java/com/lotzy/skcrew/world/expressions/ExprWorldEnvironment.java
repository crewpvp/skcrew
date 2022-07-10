package com.lotzy.skcrew.world.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import static ch.njol.skript.expressions.base.PropertyExpression.register;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import javax.annotation.Nullable;
import org.bukkit.World;
import org.bukkit.World.Environment;

@Name("World - World's environment")
@Description({"Get environment of world"})
@Examples("broadcast \"%environment of world(\"world\")%\"")
@Since("1.4")
public class ExprWorldEnvironment extends SimplePropertyExpression<World, Environment> {

    static {
        register(ExprWorldEnvironment.class, Environment.class, "(type|environment)", "world");
    }

    @Override
    @Nullable
    public Environment convert(World world) {
        return world.getEnvironment();
    }

    @Override
    public Class<? extends Environment> getReturnType() {
        return Environment.class;
    }

    @Override
    protected String getPropertyName() {
        return "Environment of world";
    }

}
