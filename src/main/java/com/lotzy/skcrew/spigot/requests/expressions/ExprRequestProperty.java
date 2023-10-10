package com.lotzy.skcrew.spigot.requests.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.requests.RequestProperty;
import org.bukkit.event.Event;

@Name("Requests - Request Property")
@Description("Used for making headers of request")
@Examples("set {_headers} to request header \"auth\" \"keycode\"")
@Since("1.6")
public class ExprRequestProperty extends SimpleExpression<RequestProperty> {

    static {
        Skript.registerExpression(ExprRequestProperty.class, RequestProperty.class, ExpressionType.COMBINED,
            "request (header|property) %string% %string%"
        );
    }

    Expression<String> key;
    Expression<String> value;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
        this.key = (Expression<String>)exprs[0];
        this.value = (Expression<String>)exprs[1];
        return true;
    }

    @Override
    protected RequestProperty[] get(Event e) {
        return new RequestProperty[] { new RequestProperty(this.key.getSingle(e),this.value.getSingle(e)) };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends RequestProperty> getReturnType() {
        return RequestProperty.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "Request Property "+this.key.getSingle(e)+ " "+this.value.getSingle(e);
    }
}