package com.lotzy.skcrew.world.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import javax.annotation.Nullable;
import org.bukkit.World;
import org.bukkit.event.Event;


@Name("World - Size of world border")
@Description("Get or set size of world border")
@Examples({"on load:",
        "\tset border size of world(\"world\") to 256"})
@Since("1.0")
public class ExprWorldBorderSize extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprWorldBorderSize.class, Number.class, ExpressionType.COMBINED,
                "[world[ ]]border size of %world% [for %timespan%]",
                "%world%'s [world[ ]]border size [for %timespan%]",
                "[the] size of %world%'s [world[ ]]border [for %timespan%]");
    }

    private Expression<World> worldExpr;
    private Expression<Timespan> timespan;

    @Nullable
    @Override
    protected Number[] get(Event e) {
        if (worldExpr == null) return new Number[0];
        World world = worldExpr.getSingle(e);
        if (world == null) return new Number[0];
        return new Number[]{world.getWorldBorder().getSize()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return worldExpr.toString(e, debug) + "'s world border size";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        worldExpr = (Expression<World>) exprs[0];
        if (exprs[1]!=null) {
            timespan = (Expression<Timespan>) exprs[1];
        }
        return true;
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.REMOVE_ALL) return null;
        return CollectionUtils.array(Number.class);
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        World world = worldExpr.getSingle(e);
        Integer time;
        if(timespan!=null) {
            time = timespan.getSingle(e).getTicks();
        } else { time = 0; }
        
        if (world == null) return;
        if (mode == ChangeMode.ADD || mode == ChangeMode.SET || mode == ChangeMode.REMOVE) {
            if (delta.length < 1 || !(delta[0] instanceof Number)) return;
            Number number = (Number) delta[0];
            double i = number.doubleValue();
            double currentSize = world.getWorldBorder().getSize();
            if (null == mode) {
                world.getWorldBorder().setSize(Math.max(0, i), time);
            } else switch (mode) {
                case ADD:
                    world.getWorldBorder().setSize(Math.max(0, currentSize + i) , time);
                    break;
                case REMOVE:
                    world.getWorldBorder().setSize(Math.max(0, currentSize - i), time);
                    break;
                default:
                    world.getWorldBorder().setSize(Math.max(0, i), time);
                    break;
            }
            return;
        }
        world.getWorldBorder().setSize(0);
    }
}
