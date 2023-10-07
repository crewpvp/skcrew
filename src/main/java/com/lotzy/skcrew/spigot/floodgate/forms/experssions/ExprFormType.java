package com.lotzy.skcrew.spigot.floodgate.forms.experssions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import javax.annotation.Nullable;
import org.geysermc.cumulus.util.FormType;

@Name("Forms - Type")
@Description({"Get type of form"})
@Examples("broadcast \"%type of last created form%\"")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class ExprFormType extends SimplePropertyExpression<Form, FormType> {

    static {
        register(ExprFormType.class, FormType.class, "type", "form");
    }

    @Override
    @Nullable
    public FormType convert(Form form) {
        return form.getType();
    }



    @Override
    public Class<? extends FormType> getReturnType() {
        return FormType.class;
    }

    @Override
    protected String getPropertyName() {
        return "Type of form";
    }

}
