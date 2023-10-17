package com.lotzy.skcrew.spigot.floodgate.forms.experssions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import org.bukkit.event.Event;

@Name("Forms - ID")
@Description("Get or set id of form to make it global")
@Examples("set id of last created form to \"MyGlobalForm\"")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class ExprIDOfForm extends SimplePropertyExpression<Form, String> {

    static {
        register(ExprIDOfForm.class, String.class, "id[entifier]", "form");
    }

    @Override
    public String convert(Form form) {
        return form.getID();
    }

    @Override
    
    public Class<?>[] acceptChange(ChangeMode mode) {
        return mode == ChangeMode.SET ? CollectionUtils.array(String.class) : null;
    }

    @Override
    public void change(Event e,  Object[] delta, ChangeMode mode) {
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
