package com.lotzy.skcrew.spigot.requests;

import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;

public class TypeRequestProperty {

    static public void register() {
        Classes.registerClass(new ClassInfo<>(RequestProperty.class, "requestproperty")
        .user("request propert(y|ies)?")
        .name("requestproperty")
        .description("Represents a request property (com.lotzy.skcrew.spigot.requests.RequestProperty class)")
        .examples("set {_pr} to request property \"key\" \"value\".")
        .since("1.6")
        .parser(new Parser<RequestProperty>() {
                @Override
                public boolean canParse(ParseContext ctx) {
                        return false;
                }

                @Override
                public String toString(RequestProperty r, int flags) {
                        return "request property "+ r.key + " " + r.value;
                }

                @Override
                public String toVariableNameString(RequestProperty r) {
                        return toString(r, 0);
                }
            })
        );
    }
}


