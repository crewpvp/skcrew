package com.lotzy.skcrew.spigot.requests.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.lotzy.skcrew.spigot.requests.RequestProperty;
import org.bukkit.event.Event;

@Name("Requests - Value of request property")
@Description("Return value of request property")
@Examples({"on load:",
        "\tset {_value} to {_property}'s value"})
@Since("1.6")
public class ExprRequestPropertyValue extends SimplePropertyExpression<RequestProperty,String> {

    static {
        register(ExprRequestPropertyValue.class, String.class,
            "value", "requestproperty");
    }

    @Override
    protected String getPropertyName() {
        return "Request property value";
    }

    @Override
    public String convert(RequestProperty f) {
        return f.getValue();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
    
    @Override
    
    public Class<?>[] acceptChange(final ChangeMode mode) {
        if (mode == ChangeMode.SET)
            return new Class[] {String.class};
        return null;
    }

    @Override
    public void change(final Event e, final  Object[] delta, final ChangeMode mode) throws UnsupportedOperationException {
        getExpr().getSingle(e).setValue((String) delta[0]);
    }
}