package com.lotzy.skcrew.spigot.packets.dataWrappers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ByteBufManipulator {
    
    static public void writeBytes(ByteBuf buffer, ByteBuf fromBuffer) {
        buffer.writeBytes(fromBuffer);
    }
    static public ByteBuf readBytes(ByteBuf buffer, int i) {
        return buffer.readBytes(i);
    }
    
    static public void writeBoolean(ByteBuf buffer, boolean value) {
        buffer.writeBoolean(value);
    }
    static public boolean readBoolean(ByteBuf buffer) {
        return buffer.readBoolean();
    }
    
    static public void writeByte(ByteBuf buffer, byte value) {
        buffer.writeByte(value);
    }
    static public byte readByte(ByteBuf buffer) {
        return buffer.readByte();
    }
    
    static public void writeUnsignedByte(ByteBuf buffer, byte value) {
        buffer.writeByte(value);
    }
    static public short readUnsignedByte(ByteBuf buffer) {
        return buffer.readUnsignedByte();
    }
    
    static public void writeShort(ByteBuf buffer, short value) {
        buffer.writeShort(value);
    }
    static public short readShort(ByteBuf buffer) {
        return buffer.readShort();
    }
    
    static public void writeUnsignedShort(ByteBuf buffer, short value) {
        buffer.writeShort(value);
    }
    static public int readUnsignedShort(ByteBuf buffer) {
        return buffer.readUnsignedShort();
    }
    
    static public void writeFloat(ByteBuf buffer, float value) {
        buffer.writeFloat(value);
    }
    static public float readFloat(ByteBuf buffer) {
        return buffer.readFloat();
    }
    
    static public void writeDouble(ByteBuf buffer, double value) {
        buffer.writeDouble(value);
    }
    static public double readDouble(ByteBuf buffer) {
        return buffer.readDouble();
    }
    
    static public void writeInt(ByteBuf buffer, int value) {
        buffer.writeInt(value);
    }
    static public int readInt(ByteBuf buffer) {
        return buffer.readInt();
    }
    
    static public void writeLong(ByteBuf buffer, long value) {
        buffer.writeLong(value);
    }
    static public long readLong(ByteBuf buffer) {
        return buffer.readLong();
    }
    
    static public void writeString(ByteBuf buffer, CharSequence charsequence) {
        if (charsequence.length() > 32767) {
            int j = charsequence.length();

            throw new EncoderException("String too big (was " + j + " characters, max 32767)");
        } else {
            int k = ByteBufUtil.utf8MaxBytes(charsequence);
            ByteBuf bytebuf1 = buffer.alloc().buffer(k);

            try {
                int l = ByteBufUtil.writeUtf8(bytebuf1, charsequence);
                int i1 = ByteBufUtil.utf8MaxBytes(32767);

                if (l > i1) {
                    throw new EncoderException("String too big (was " + l + " bytes encoded, max " + i1 + ")");
                }

                writeVarInt(buffer, l);
                buffer.writeBytes(bytebuf1);
            } finally {
                bytebuf1.release();
            }

        }
    }
    static public String readString(ByteBuf buffer) {
        int j = ByteBufUtil.utf8MaxBytes(32767);
        int k = readVarInt(buffer);

        if (k > j) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + k + " > " + j + ")");
        } else if (k < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            int l = buffer.readableBytes();

            if (k > l) {
                throw new DecoderException("Not enough bytes in buffer, expected " + k + ", but got " + l);
            } else {
                String s = buffer.toString(buffer.readerIndex(), k, StandardCharsets.UTF_8);

                buffer.readerIndex(buffer.readerIndex() + k);
                if (s.length() > 32767) {
                    int i1 = s.length();

                    throw new DecoderException("The received string length is longer than maximum allowed (" + i1 + " > 32767)");
                } else {
                    return s;
                }
            }
        }
    }
    
    static public void writePosition(ByteBuf buffer, BlockPositionWrapper blockPosition) {
        buffer.writeLong(blockPosition.asLong());
    }
    static public BlockPositionWrapper readPosition(ByteBuf buffer) {
        return new BlockPositionWrapper(buffer.readLong());
    }
    
    static public void writeUUID(ByteBuf buffer, UUID uuid) {
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
    }
    static public UUID readUUID(ByteBuf buffer) {
        return new UUID(buffer.readLong(), buffer.readLong());
    }
    
    static public void writeAngle(ByteBuf buffer, double angle) {
        buffer.writeByte((byte)(angle * 256.0 / 360.0));
    }
    static public double readAngle(ByteBuf buffer) {
        return buffer.readByte() * 360.0 / 256.0;        
    }
    
    static public void writeVarInt(ByteBuf buffer, int value) {
        while ((value & -128) != 0) {
            buffer.writeByte(value & 127 | 128);
            value >>>= 7;
        }

        buffer.writeByte(value);
    }
    static public int readVarInt(ByteBuf buffer) {
        int i = 0;
        int j = 0;

        byte b0;

        do {
            b0 = buffer.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }
    
    static public void writeVarLong(ByteBuf buffer, long value) {
        while ((value & -128L) != 0L) {
            buffer.writeByte((int) (value & 127L) | 128);
            value >>>= 7;
        }

        buffer.writeByte((int) value);
    }
    static public long readVarLong(ByteBuf buffer) {
        long i = 0L;
        int j = 0;

        byte b0;

        do {
            b0 = buffer.readByte();
            i |= (long) (b0 & 127) << j++ * 7;
            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }
    
    static public void writeUTF8(ByteBuf buffer, String value) {
        buffer.writeBytes(value.getBytes(StandardCharsets.UTF_8));
    }
    static public String readUTF8(ByteBuf buffer, int len) {
        return buffer.readBytes(len).toString(StandardCharsets.UTF_8);
    }
}
