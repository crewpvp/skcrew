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
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.shared.sockets.data.Signal;
import org.bukkit.event.Event;

@Name("Sockets - Signal")
@Description("Return new signal (com.lotzy.skcrew.shared.sockets.data.Signal class)")
@Examples({"on load:",
        "\tsend signal (signal \"zalopa\" with data 1) to server \"lobby\""})
@Since("3.0")
public class ExprSignal extends SimpleExpression<Signal> {

    static {
        Skript.registerExpression(ExprSignal.class, Signal.class, ExpressionType.COMBINED,
            "signal [(with key|keyed)|(with name|named)] %string% (and|with) [data] %objects%");
    }
    
    Expression<String> key;
    Expression<Object> data;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        key = (Expression<String>) expr[0];
        data = LiteralUtils.defendExpression(expr[1]);
        return LiteralUtils.canInitSafely(data);
    }

    @Override
    protected Signal[] get(Event e) {
        return new Signal[] {  new Signal(key.getSingle(e),data.getAll(e)) };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Signal> getReturnType() {
        return Signal.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Signal: " + key.toString(e,debug);
    }
}