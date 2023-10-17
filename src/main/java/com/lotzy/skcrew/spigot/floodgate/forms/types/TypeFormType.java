package com.lotzy.skcrew.spigot.floodgate.forms.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.geysermc.cumulus.form.util.FormType;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.util.EnumUtils;

public class TypeFormType {
    static {
        final EnumUtils<FormType> formTypes = new EnumUtils<>(FormType.class, "form types");
        Classes.registerClass(new ClassInfo<>(FormType.class, "formtype")
            .user("form ?types?")
            .name("Form Type")
            .description("Form types (represents org.geysermc.cumulus.form.util.FormType class)")
            .usage(formTypes.getAllNames())
            .since("3.0")
            .defaultExpression(new EventValueExpression<>(FormType.class))
            .parser(new Parser<FormType>() {
                    @Override
                    public FormType parse(String s, ParseContext context) {
                        return formTypes.parse(s);
                    }

                    @Override
                    public String toString(FormType o, int flags) {
                        return formTypes.toString(o, flags);
                    }

                    @Override
                    public String toVariableNameString(FormType o) {
                        return o.name();
                    }
            }));
    }
}
