package com.lotzy.skcrew.spigot.floodgate.forms.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormCloseEvent.CloseReason;

public class TypeCloseReason {
    static public void register() {
        Classes.registerClass(new ClassInfo<>(CloseReason.class, "closereason")
            .user("close ?reasons?")
            .name("Close Reason")
            .description("Close reason (represents com.lotzy.skcrew.spigot.floodgate.forms.events.FormCloseEvent.CloseReason class)")
            .since("3.0")
            .parser(new Parser<CloseReason>() {
                @Override
                public CloseReason parse(String s, ParseContext context) {
                    return null;
                }

                @Override
                public String toString(CloseReason o, int flags) {
                    return o.toString().toLowerCase().replace('_', ' ');
                }

                @Override
                public String toVariableNameString(CloseReason o) {
                    return o.toString().toLowerCase().replace('_', ' ');
                }
            }));
    }
}
