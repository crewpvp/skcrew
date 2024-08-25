package com.lotzy.skcrew.spigot.packets.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import com.lotzy.skcrew.spigot.packets.PacketReflection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.io.StreamCorruptedException;

public class TypePacket {
      static public void register() {
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
            }).serializer(new Serializer<Object>() {
                @Override
                public Fields serialize(final Object packet) {
                    ByteBuf buffer = PacketReflection.decodePacket(packet);
                    String name = packet.getClass().getSimpleName();
                    final Fields f = new Fields();
                    f.putObject("type", name);
                    f.putObject("bytes", ByteBufUtil.getBytes(buffer));
                    return f;
                }

                @Override
                public void deserialize(final Object o, final Fields f) {
                    assert false;
                }

                @Override
                public Object deserialize(final Fields f) throws StreamCorruptedException {
                    try {
                        String name = f.getObject("type", String.class);
                        byte[] bytes = f.getObject("bytes", byte[].class);
                        ByteBuf buffer = Unpooled.buffer();
                        buffer.writeBytes(bytes);
                        return PacketReflection.createPacket(name, buffer);
                    } catch (Exception ex) {
                        return null;
                    }
                }

                @Override
                public boolean canBeInstantiated() {
                    return false; // no nullary constructor - also, saving the location manually prevents errors should Location ever be changed
                }

                @Override
                public boolean mustSyncDeserialization() {
                    return true;
                }
            }));
    }  
}
