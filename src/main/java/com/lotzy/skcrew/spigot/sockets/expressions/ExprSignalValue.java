package com.lotzy.skcrew.spigot.sockets.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.sockets.SignalEvent;
import com.lotzy.skcrew.spigot.sockets.events.EvtSignal;
import javax.annotation.Nullable;
import org.bukkit.event.Event;

@Name("Sockets - Signal value")
@Description({"Get value of signal",
        "Can be used in signal event"})
@Examples({"on signal:",
        "\tbroadcast \"%signal value%\""})
@Since("1.6")
public class ExprSignalValue extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprSignalValue.class, Object.class, ExpressionType.COMBINED,
            "[event(-| )]signal(-| )value"
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
        if (!(skriptEvent instanceof EvtSignal)) {
            Skript.error("You can't get signal value outside signal event.",ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    protected Object[] get(Event e) {
        return new Object[] { ((SignalEvent)e).getValue() };
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
    public String toString(@Nullable Event e, boolean debug) {
        return "signal value";
    }

}
