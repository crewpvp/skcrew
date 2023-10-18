package com.lotzy.skcrew.spigot.floodgate.forms.experssions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormCloseEvent.CloseReason;
import org.bukkit.event.Event;

@Name("Forms - Close reasons")
@Description({"All reasons of form close"})
@Examples({"close", "invalid", "submit"})
@RequiredPlugins("Floodgate")
@Since("3.0")
public class ExprCloseReasons extends SimpleExpression<CloseReason> {

    static {
        Skript.registerExpression(ExprCloseReasons.class, CloseReason.class, ExpressionType.SIMPLE, 
            "close", "invalid[ response]","(submit|success)");
    }
    
    int pattern;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        pattern = matchedPattern;
        return true;
    }
    
    @Override
    protected CloseReason[] get(Event event) {
        return new CloseReason[] { CloseReason.values()[pattern] };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends CloseReason> getReturnType() {
        return CloseReason.class;
    }

    @Override
    public String toString( Event event, boolean debug) {
        return "Close reason "+ (CloseReason.values()[pattern]).toString();
    }
}
