package com.lotzy.skcrew.world.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import javax.annotation.Nullable;
import org.bukkit.event.Event;
import org.bukkit.World.Environment;
        
@Name("World - Environment")
@Description("Represent all world environments")
@Since("1.4")
public class ExprEnvironment extends SimpleExpression<Environment> {

    static {
        Skript.registerExpression(ExprEnvironment.class, Environment.class, ExpressionType.SIMPLE,
                "[(a|the)] nether","[(a|the)] end","[(a|the)] (normal|overworld)", "[(a|the)] custom");
    }
    int pattern;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        pattern = matchedPattern;
        return true;
    }

    @Override
    @Nullable
    protected Environment[] get(Event e) {
        switch(pattern) {
            case 0:
               return new Environment[] { Environment.NETHER };
            case 1:
               return new Environment[] { Environment.THE_END };
            case 2:
                return new Environment[] { Environment.NORMAL };
            default:
                return new Environment[] { Environment.CUSTOM };
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Environment> getReturnType() {
        return Environment.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "world environments";
    }

} 