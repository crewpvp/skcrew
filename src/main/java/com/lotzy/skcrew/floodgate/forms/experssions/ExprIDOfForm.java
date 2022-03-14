package com.lotzy.skcrew.floodgate.forms.experssions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.floodgate.forms.Form;
import javax.annotation.Nullable;
import org.bukkit.event.Event;

public class ExprIDOfForm extends SimplePropertyExpression<Form, String> {

    static {
        register(ExprIDOfForm.class, String.class, "id[entifier]", "form");
    }

    @Override
    @Nullable
    public String convert(Form form) {
        return form.getID();
    }

    @Override
    @Nullable
    public Class<?>[] acceptChange(ChangeMode mode) {
        return mode == ChangeMode.SET ? CollectionUtils.array(String.class) : null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (delta == null || delta[0] == null) {
            return;
        }
        String id = (String) delta[0];
        Form[] forms = getExpr().getArray(e);
        for (Form form : forms) {
            if (form != null) {
                form.setID(id);
            }
        }
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "id";
    }

}
