package com.lotzy.skcrew.spigot.packets.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.lotzy.skcrew.spigot.packets.PacketReflection;

public class TypePacket {
      static {
        Classes.registerClass(new ClassInfo<>(PacketReflection.PacketClass, "packet")
            .user("packet?")
            .name("Packet")
            .description("Represents a real nms Packet")
            .since("3.4")
            .parser(new Parser<Object>() {
                @Override
                public Object parse(String packet, ParseContext arg1) {
                    return null;
                }

                @Override
                public boolean canParse(final ParseContext context) {
                    return false;
                }

                @Override
                public String toString(Object packet, int arg1) {
                    return packet.getClass().getSimpleName();
                }

                @Override
                public String toVariableNameString(Object packet) {
                    return packet.getClass().getSimpleName();
                }
            }));
    }  
}
