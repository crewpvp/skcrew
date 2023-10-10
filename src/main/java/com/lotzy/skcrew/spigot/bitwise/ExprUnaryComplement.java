package com.lotzy.skcrew.spigot.bitwise;

import org.bukkit.event.Event;
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

@Name("Bitwise - Unary complement")
@Description("Unary complement of number")
@Examples({"on load:",
        "\tset {_num} to ~5"})
@Since("3.0")
public class ExprUnaryComplement extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprUnaryComplement.class, Integer.class, ExpressionType.COMBINED,"\\~[ ]%number%");
    }

    private Expression<Number> n1;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        n1 = (Expression<Number>) expr[0];
        return true;
    }

    @Override
    protected Integer[] get(Event event) {
        return new Integer[] {~n1.getSingle(event).intValue()};
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
    public String toString(Event event, boolean debug) {
        return "Unary complement ~"+n1.toString(event,debug);
    }
}
