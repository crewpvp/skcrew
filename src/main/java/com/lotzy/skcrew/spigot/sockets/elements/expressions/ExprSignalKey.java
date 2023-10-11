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

@Name("Sockets - Signal key")
@Description("Return key of signal")
@Examples({"on signal:",
        "\tbroadcast key of event-signal"})
@Since("3.0")
public class ExprSignalKey extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprSignalKey.class, String.class, ExpressionType.COMBINED,
            "(key|signal name) of %signal%", "%signal%'s (signal name|key)");
    }
    
    Expression<Signal> signal;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        signal = (Expression<Signal>) expr[0];
        return true;
    }

    @Override
    protected String[] get(Event e) {
        return new String[] {  signal.getSingle(e).getKey() };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Key of signal: " + signal.toString(e,debug);
    }
}