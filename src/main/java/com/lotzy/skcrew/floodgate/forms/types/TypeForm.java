package com.lotzy.skcrew.floodgate.forms.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.lotzy.skcrew.floodgate.forms.Form;
public class TypeForm {
    static {
        Classes.registerClass(new ClassInfo<>(Form.class, "form")
                .user("forms?")
                .parser(new Parser<Form>() {
                    @Override
                    public Form parse(String s, ParseContext context) {
                        return null;
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
