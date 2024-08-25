package com.lotzy.skcrew.spigot.sockets.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.lotzy.skcrew.shared.sockets.data.Signal;

public class TypeSignal {

    static public void register() {
        Classes.registerClass(new ClassInfo<>(Signal.class, "signal")
            .defaultExpression(new EventValueExpression<>(Signal.class))
            .user("signals?")
            .name("signal")
            .description("Represents a signal (com.lotzy.skcrew.shared.data.Signal class)")
            .since("3.0")
            .parser(new Parser<Signal>() {
                @Override
                public Signal parse(String name, ParseContext arg1) {
                    return null;
                }

                @Override
                public boolean canParse(final ParseContext context) {
                    return false;
                }

                @Override
                public String toString(Signal signal, int arg1) {
                    return signal.toString();
                }

                @Override
                public String toVariableNameString(Signal signal) {
                    return signal.toString();
                }
            }));
    }
}