package com.lotzy.skcrew.spigot.sockets.elements.expressions;

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
import com.lotzy.skcrew.shared.sockets.data.Signal;
import org.bukkit.event.Event;

@Name("Sockets - Signal data")
@Description("Return data of signal")
@Examples({"on signal:",
        "\tbroadcast \"%data of event-signal\""})
@Since("3.0")
public class ExprSignalData extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprSignalData.class, Object.class, ExpressionType.COMBINED,
            "data of %signal%", "%signal%'s data");
    }
    
    Expression<Signal> signal;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        signal = (Expression<Signal>) expr[0];
        return true;
    }

    @Override
    protected Object[] get(Event e) {
        return signal.getSingle(e).getData();
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Object> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Data of signal: " + signal.toString(e,debug);
    }
}