package com.lotzy.skcrew.requests.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.lotzy.skcrew.requests.RequestProperty;
import javax.annotation.Nullable;
import org.bukkit.event.Event;

@Name("Requests - Key of request property")
@Description("Return key of request property")
@Examples({"on load:",
        "\tset {_key} to {_property}'s key"})
@Since("1.6")
public class ExprRequestPropertyKey extends SimplePropertyExpression<RequestProperty,String> {

    static {
        register(ExprRequestPropertyKey.class, String.class,
            "key", "requestproperty");
    }

    @Override
    protected String getPropertyName() {
        return "Request property key";
    }

    @Override
    public String convert(RequestProperty f) {
        return f.getKey();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
    
    @Override
    @Nullable
    public Class<?>[] acceptChange(final ChangeMode mode) {
        if (mode == ChangeMode.SET)
            return new Class[] {String.class};
        return null;
    }

    @Override
    public void change(final Event e, final @Nullable Object[] delta, final ChangeMode mode) throws UnsupportedOperationException {
        getExpr().getSingle(e).setKey((String) delta[0]);
    }

}