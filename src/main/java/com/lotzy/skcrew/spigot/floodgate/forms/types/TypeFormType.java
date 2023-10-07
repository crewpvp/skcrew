package com.lotzy.skcrew.spigot.floodgate.forms.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.geysermc.cumulus.util.FormType;

public class TypeFormType {
    static {
        Classes.registerClass(new ClassInfo<>(FormType.class, "typeform")
                .user("typeforms?")
                .name("Form type")
                .description("Represent a form type (org.geysermc.cumulus.util.FormType class)")
                .since("1.0")
                .parser(new Parser<FormType>() {
                    @Override
                    public FormType parse(String s, ParseContext context) {
                        return null;
                    }

                    @Override
                    public String toString(FormType o, int flags) {
                        return o.toString();
                    }

                    @Override
                    public String toVariableNameString(FormType o) {
                        return o.toString();
                    }
                }));
            }
}
