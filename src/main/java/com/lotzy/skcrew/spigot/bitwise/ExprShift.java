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

@Name("Bitwise - Shift")
@Description("Left, right and unsigned right shift operations")
@Examples({"on load:",
        "\tset {_num} to 2 >> 2",
        "\tset {_num} to 2 >>> 2",
        "\tset {_num} to 2 << 2"})
@Since("3.0")
public class ExprShift extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprShift.class, Integer.class, ExpressionType.COMBINED,
            "%number%[ ]\\>\\>[ ]%number%",
            "%number%[ ]\\<\\<[ ]%number%",
            "%number%[ ]\\>\\>\\>[ ]%number%"
        );
    }

    private Expression<Number> n1;
    private Expression<Number> n2;
    int pattern;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        n1 = (Expression<Number>) expr[0];
        n2 = (Expression<Number>) expr[1];
        pattern = matchedPattern;
        return true;
    }

    @Override
    protected Integer[] get(Event event) {
        switch (pattern) {
            case 0: 
                return new Integer[] {n1.getSingle(event).intValue() >> n2.getSingle(event).intValue()};
            case 1:
                return new Integer[] {n1.getSingle(event).intValue() << n2.getSingle(event).intValue()};
            default:
                return new Integer[] {n1.getSingle(event).intValue() >>> n2.getSingle(event).intValue()};
        }
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
        switch (pattern) {
            case 0: 
                return "Shift right "+n1.toString(event,debug)+" >> "+n2.toString(event,debug);
            case 1:
                return "Shift left "+n1.toString(event,debug)+" << "+n2.toString(event,debug);
            default:
                return "Unsigned shift right "+n1.toString(event,debug)+" << "+n2.toString(event,debug);
        }
    }
}
