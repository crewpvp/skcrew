package com.lotzy.skcrew.spigot.packets.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import io.netty.buffer.ByteBuf;

public class ByteBufType {
      static {
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
            }));
    }  
}
