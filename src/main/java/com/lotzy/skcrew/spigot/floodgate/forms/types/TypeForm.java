package com.lotzy.skcrew.spigot.floodgate.forms.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;

public class TypeForm {
    static public void register() {
        Classes.registerClass(new ClassInfo<>(Form.class, "form")
            .user("forms?")
            .name("Form")
            .description("Represent a form (com.lotzy.skcrew.spigot.floodgate.forms.Form class)")
            .since("1.0")
            .parser(new Parser<Form>() {
                @Override
                public Form parse(String s, ParseContext context) {
                    return null;
                }

                @Override
                public boolean canParse(ParseContext ctx) {
                    return false;
                }
                
                @Override
                public String toString(Form o, int flags) {
                    return o.toString();
                }

                @Override
                public String toVariableNameString(Form o) {
                    return o.toString();
                }
            }));
        }
}
