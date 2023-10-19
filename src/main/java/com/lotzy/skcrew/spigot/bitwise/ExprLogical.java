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

@Name("Bitwise - Logical")
@Description("Some logical operations like OR, AND, XOR")
@Examples({"on load:",
        "\tset {_num} to true | false",
        "\tset {_num} to false & false",
        "\tset {_num} to 2 | 3",
        "\tset {_num} to 3 & 3",
        "\tset {_num} to 4 ^ 4"})
@Since("3.0")
public class ExprLogical extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprLogical.class, Object.class, ExpressionType.COMBINED,
            "%boolean%[ ]\\|\\|[ ]%boolean%",
            "%boolean%[ ]\\&\\&[ ]%boolean%",
            "%number%[ ]\\|[ ]%number%",
            "%number%[ ]\\&[ ]%number%"
        );
    }

    private Expression<Object> n1;
    private Expression<Object> n2;
    int pattern;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        n1 = (Expression<Object>) expr[0];
        n2 = (Expression<Object>) expr[1];
        pattern = matchedPattern;
        return true;
    }

    @Override
    protected Object[] get(Event event) {
        switch (pattern) {
            case 0: 
                return new Object[] {(boolean)n1.getSingle(event) | (boolean)n2.getSingle(event)};
            case 1:
                return new Object[] {(boolean)n1.getSingle(event) & (boolean)n2.getSingle(event)};
            case 2:
                return new Integer[] {((Number) n1.getSingle(event)).intValue() | ((Number) n2.getSingle(event)).intValue()};
            default:
                return new Integer[] {((Number) n1.getSingle(event)).intValue() & ((Number) n2.getSingle(event)).intValue()};
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Object> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(Event event, boolean debug) {
        switch (pattern) {
            case 0: 
                return "Operation or with booleans "+n1.toString(event,debug)+" | "+n2.toString(event,debug);
            case 1:
                return "Operation and with booleans "+n1.toString(event,debug)+" & "+n2.toString(event,debug);
            case 2: 
                return "Operation or with numbers "+n1.toString(event,debug)+" | "+n2.toString(event,debug);
            default:
                return "Operation and with numbers "+n1.toString(event,debug)+" & "+n2.toString(event,debug);
        }
    }
}
