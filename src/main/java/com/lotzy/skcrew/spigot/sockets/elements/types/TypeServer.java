package com.lotzy.skcrew.spigot.sockets.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.lotzy.skcrew.shared.sockets.data.SpigotServer;

public class TypeServer {

    static public void register() {
        Classes.registerClass(new ClassInfo<>(SpigotServer.class, "server")
            .defaultExpression(new EventValueExpression<>(SpigotServer.class))
            .user("servers?")
            .name("Server")
            .description("Represents a network server (com.lotzy.skcrew.shared.data.SpigotServer class)")
            .since("3.0")
            .parser(new Parser<SpigotServer>() {
                @Override
                public SpigotServer parse(String server, ParseContext arg1) {
                    return null;
                }

                @Override
                public boolean canParse(final ParseContext context) {
                    return false;
                }

                @Override
                public String toString(SpigotServer server, int arg1) {
                    return server.getName();
                }

                @Override
                public String toVariableNameString(SpigotServer server) {
                    return server.getName();
                }
            }));
    }
}