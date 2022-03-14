package com.lotzy.skcrew.floodgate.forms.experssions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.lotzy.skcrew.floodgate.forms.Form;
import javax.annotation.Nullable;
import org.geysermc.cumulus.util.FormType;

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
