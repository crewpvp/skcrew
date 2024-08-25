package com.lotzy.skcrew.spigot.packets.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.io.StreamCorruptedException;

public class ByteBufType {
      static public void register() {
        Classes.registerClass(new ClassInfo<>(ByteBuf.class, "bytebuf")
            .user("bytebuf?")
            .name("ByteBuf")
            .description("Represents a ByteBuf (io.netty.buffer.ByteBuf class)")
            .since("3.4")
            .parser(new Parser<ByteBuf>() {
                @Override
                public ByteBuf parse(String buffer, ParseContext arg1) {
                    return null;
                }

                @Override
                public boolean canParse(final ParseContext context) {
                    return false;
                }

                @Override
                public String toString(ByteBuf buffer, int arg1) {
                    return "buffer(read-index: " + buffer.readerIndex()+
                            ", write-index: " + buffer.writerIndex()+
                            ", capacity: "+buffer.capacity()+")";
                }

                @Override
                public String toVariableNameString(ByteBuf buffer) {
                    return "buffer(read-index: " + buffer.readerIndex()+
                            ", write-index: " + buffer.writerIndex()+
                            ", capacity: "+buffer.capacity()+")";
                }
            }).serializer(new Serializer<ByteBuf>() {
                @Override
                public Fields serialize(final ByteBuf buffer) {
                    final Fields f = new Fields();
                    f.putObject("bytes", ByteBufUtil.getBytes(buffer));
                    return f;
                }

                @Override
                public void deserialize(final ByteBuf o, final Fields f) {
                    assert false;
                }

                @Override
                public ByteBuf deserialize(final Fields f) throws StreamCorruptedException {
                    try {
                        byte[] bytes = f.getObject("bytes", byte[].class);
                        ByteBuf buffer = Unpooled.buffer();
                        buffer.writeBytes(bytes);
                        return buffer;
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
