package kuwg.packetapi.util;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class ByteBufUtil {
    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;
    public static int readVarInt(ByteBuf byteBuf) {
        int value = 0;
        int position = 0;
        byte currentByte;
        while (true) {
            currentByte = byteBuf.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;
            if ((currentByte & CONTINUE_BIT) == 0) break;
            position += 7;
            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }
    public static void writeVarInt(ByteBuf byteBuf, int value) {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                byteBuf.writeByte(value);
                return;
            }

            byteBuf.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
            value >>>= 7;
        }
    }
    public static void writeString(ByteBuf byteBuf, String s) {
        writeString(byteBuf, s, 32767);
    }

    public static void writeString(ByteBuf byteBuf, String s, int maxLen) {
        writeString(byteBuf, s, maxLen, true);
    }

    public static String maximizeLength(String msg, int maxLength) {
        if (msg.length() > maxLength) {
            return msg.substring(0, maxLength);
        } else {
            return msg;
        }
    }

    public static void writeString(ByteBuf byteBuf, String s, int maxLen, boolean substr) {
        if (substr) {
            s = maximizeLength(s, maxLen);
        }
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        if (!substr && bytes.length > maxLen) {
            throw new IllegalStateException("String too big (was " + bytes.length + " bytes encoded, max " + maxLen + ")");
        } else {
            writeVarInt(byteBuf, bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }
}
