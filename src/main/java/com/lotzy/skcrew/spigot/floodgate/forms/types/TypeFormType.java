package com.lotzy.skcrew.spigot.floodgate.forms.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.geysermc.cumulus.form.util.FormType;

public class TypeFormType {
    static public void register() {
        Classes.registerClass(new ClassInfo<>(FormType.class, "formtype")
            .user("form ?types?")
            .name("Form Type")
            .description("Form types (represents org.geysermc.cumulus.form.util.FormType class)")
            .since("3.0")
            .parser(new Parser<FormType>() {
                @Override
                public FormType parse(String s, ParseContext context) {
                    return null;
                }

                @Override
                public String toString(FormType o, int flags) {
                    return o.toString().toLowerCase().replace('_', ' ')+" form";
                }

                @Override
                public String toVariableNameString(FormType o) {
                    return o.toString().toLowerCase().replace('_', ' ')+" form";
                }
            }));
    }
}
