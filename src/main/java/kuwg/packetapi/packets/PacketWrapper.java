package kuwg.packetapi.packets;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.events.PacketEvent;
import kuwg.packetapi.player.PacketPlayer;
import kuwg.packetapi.util.ByteBufUtil;
import kuwg.packetapi.util.PAPIVer;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;

@SuppressWarnings("unused")
public abstract class PacketWrapper<T> {
    protected final ByteBuf buffer;
    protected final Player sender;
    private final EnumPacketDirection direction;
    public PacketWrapper(ByteBuf buffer, Player sender, EnumPacketDirection direction) {
        this.buffer = buffer;
        this.sender = sender;
        this.direction = direction;
    }

    public PacketWrapper(PacketEvent event){
        this(event.getByteBuf(), event.getPlayer(), event.getEnumPacketDirection());
    }

    public void write(){}
    public void read(){}

    public final ByteBuf buf() {
        return buffer;
    }

    public final Player getPlayer() {
        return sender;
    }

    public final EnumPacketDirection getEnumPacketDirection() {
        return direction;
    }









    public byte readByte() {
        return buffer.readByte();
    }

    public void writeByte(int value) {
        buffer.writeByte(value);
    }

    public short readUnsignedByte() {
        return buffer.readUnsignedByte();
    }

    public boolean readBoolean() {
        return readByte() != 0;
    }

    public void writeBoolean(boolean value) {
        writeByte(value ? 1 : 0);
    }

    public int readInt() {
        return buffer.readInt();
    }

    public void writeInt(int value) {
        buffer.writeInt(value);
    }

    public int readVarInt() {
        int value = 0;
        int length = 0;
        byte currentByte;
        do {
            currentByte = readByte();
            value |= (currentByte & 0x7F) << (length * 7);
            length++;
            if (length > 5) {
                throw new RuntimeException("VarInt is too large. Must be smaller than 5 bytes.");
            }
        } while ((currentByte & 0x80) == 0x80);
        return value;
    }

    public void writeVarInt(int value) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                writeByte(value);
                break;
            }
            writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
    }

    public <K, V> Map<K, V> readMap(Reader<K> keyFunction, Reader<V> valueFunction) {
        int size = readVarInt();
        Map<K, V> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            K key = keyFunction.apply(this);
            V value = valueFunction.apply(this);
            map.put(key, value);
        }
        return map;
    }

    public <K, V> void writeMap(Map<K, V> map, Writer<K> keyConsumer, Writer<V> valueConsumer) {
        writeVarInt(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            keyConsumer.accept(this, key);
            valueConsumer.accept(this, value);
        }
    }

    public String readString() {
        return readString(32767);
    }

    public String readString(int maxLen) {
        int j = readVarInt();
        if (j > maxLen * 4) {
            throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + maxLen * 4 + ")");
        } else if (j < 0) {
            throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            String s = buffer.toString(buffer.readerIndex(), j, StandardCharsets.UTF_8);
            buffer.readerIndex(buffer.readerIndex()+j);
            if (s.length() > maxLen) {
                throw new RuntimeException("The received string length is longer than maximum allowed (" + j + " > " + maxLen + ")");
            } else {
                return s;
            }
        }
    }


    public void writeString(String s) {
        writeString(s, 32767);
    }

    public void writeString(String s, int maxLen) {
        writeString(s, maxLen, true);
    }

    public static String maximizeLength(String msg, int maxLength) {
        if (msg.length() > maxLength) {
            return msg.substring(0, maxLength);
        } else {
            return msg;
        }
    }
    protected void prepareForWrite(final int id){
        ByteBufUtil.writeVarInt(this.buffer.clear(), id);
    }
    protected PacketPlayer getPacketPlayer(){
        return PacketAPI.getInstance().getPacketPlayer(getPlayer());
    }
    public void writeString(String s, int maxLen, boolean substr) {
        if (substr) {
            s = maximizeLength(s, maxLen);
        }
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        if (!substr && bytes.length > maxLen) {
            throw new IllegalStateException("String too big (was " + bytes.length + " bytes encoded, max " + maxLen + ")");
        } else {
            writeVarInt(bytes.length);
            buffer.writeBytes(bytes);
        }
    }


    public int readUnsignedShort() {
        return buffer.readUnsignedShort();
    }

    public short readShort() {
        return buffer.readShort();
    }

    public void writeShort(int value) {
        buffer.writeShort(value);
    }

    public int readVarShort() {
        int low = readUnsignedShort();
        int high = 0;
        if ((low & 0x8000) != 0) {
            low = low & 0x7FFF;
            high = readUnsignedByte();
        }
        return ((high & 0xFF) << 15) | low;
    }

    public void writeVarShort(int value) {
        int low = value & 0x7FFF;
        int high = (value & 0x7F8000) >> 15;
        if (high != 0) {
            low = low | 0x8000;
        }
        writeShort(low);
        if (high != 0) {
            writeByte(high);
        }
    }

    public long readLong() {
        return buffer.readLong();
    }

    public void writeLong(long value) {
        buffer.writeLong(value);
    }

    public long readVarLong() {
        long value = 0;
        int size = 0;
        int b;
        while (((b = readByte()) & 0x80) == 0x80) {
            value |= (long) (b & 0x7F) << (size++ * 7);
        }
        return value | ((long) (b & 0x7F) << (size * 7));
    }

    public void writeVarLong(long l) {
        while ((l & ~0x7F) != 0) {
            this.writeByte((int) (l & 0x7F) | 0x80);
            l >>>= 7;
        }

        this.writeByte((int) l);
    }

    public float readFloat() {
        return buffer.readFloat();
    }

    public void writeFloat(float value) {
        buffer.writeFloat(value);
    }

    public double readDouble() {
        return buffer.readDouble();
    }

    public void writeDouble(double value) {
        buffer.writeDouble(value);
    }

    public byte[] readRemainingBytes() {
        return readBytes(buffer.readableBytes());
    }

    public byte[] readBytes(int size) {
        byte[] bytes = new byte[size];
        buffer.readBytes(bytes);
        return bytes;
    }

    public void writeBytes(byte[] array) {
        buffer.writeBytes(array);
    }

    public byte[] readByteArray(int maxLength) {
        int len = readVarInt();
        if (len > maxLength) {
            throw new RuntimeException("The received byte array length is longer than maximum allowed (" + len + " > " + maxLength + ")");
        }
        return readBytes(len);
    }

    public byte[] readByteArray() {
        return readByteArray(buffer.readableBytes());
    }

    public void writeByteArray(byte[] array) {
        writeVarInt(array.length);
        writeBytes(array);
    }

    public int[] readVarIntArray() {
        int readableBytes = buffer.readableBytes();
        int size = readVarInt();
        if (size > readableBytes) {
            throw new IllegalStateException("VarIntArray with size " + size + " is bigger than allowed " + readableBytes);
        }

        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = readVarInt();
        }
        return array;
    }

    public void writeVarIntArray(int[] array) {
        writeVarInt(array.length);
        for (int i : array) {
            writeVarInt(i);
        }
    }

    public long[] readLongArray(int size) {
        long[] array = new long[size];

        for (int i = 0; i < array.length; i++) {
            array[i] = readLong();
        }
        return array;
    }

    public byte[] readByteArrayOfSize(int size) {
        byte[] array = new byte[size];
        buffer.readBytes(array);
        return array;
    }

    public void writeByteArrayOfSize(byte[] array) {
        buffer.writeBytes(array);
    }

    public int[] readVarIntArrayOfSize(int size) {
        int[] array = new int[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = readVarInt();
        }
        return array;
    }

    public void writeVarIntArrayOfSize(int[] array) {
        for (int i : array) {
            writeVarInt(i);
        }
    }

    public long[] readLongArray() {
        int readableBytes = buffer.readableBytes() / 8;
        int size = readVarInt();
        if (size > readableBytes) {
            throw new IllegalStateException("LongArray with size " + size + " is bigger than allowed " + readableBytes);
        }
        long[] array = new long[size];

        for (int i = 0; i < array.length; i++) {
            array[i] = readLong();
        }
        return array;
    }

    public void writeLongArray(long[] array) {
        writeVarInt(array.length);
        for (long l : array) {
            writeLong(l);
        }
    }

    public UUID readUUID() {
        long mostSigBits = readLong();
        long leastSigBits = readLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    public void writeUUID(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }





    public void writePublicKey(PublicKey publicKey) {
        writeByteArray(publicKey.getEncoded());
    }

    protected final PAPIVer version = PacketAPI.getVer();

    public static <K> IntFunction<K> limitValue(IntFunction<K> function, int limit) {
        return i -> {
            if (i > limit) {
                throw new RuntimeException("Value " + i + " is larger than limit " + limit);
            }
            return function.apply(i);
        };
    }

    public BitSet readBitSet() {
        return BitSet.valueOf(readLongArray());
    }

    public void writeBitSet(BitSet bitSet) {
        writeLongArray(bitSet.toLongArray());
    }


    public <X extends Enum<X>> EnumSet<X> readEnumSet(Class<X> enumClazz) {
        X[] values = enumClazz.getEnumConstants();
        byte[] bytes = new byte[-Math.floorDiv(-values.length, 8)];
        buffer.readBytes(bytes);
        BitSet bitSet = BitSet.valueOf(bytes);
        EnumSet<X> set = EnumSet.noneOf(enumClazz);
        for (int i = 0; i < values.length; i++) {
            if (bitSet.get(i)) {
                set.add(values[i]);
            }
        }
        return set;
    }

    public <X extends Enum<X>> void writeEnumSet(EnumSet<X> set, Class<X> enumClazz) {
        X[] values = enumClazz.getEnumConstants();
        BitSet bitSet = new BitSet(values.length);
        for (int i = 0; i < values.length; i++) {
            if (set.contains(values[i])) {
                bitSet.set(i);
            }
        }
        writeBytes(Arrays.copyOf(bitSet.toByteArray(), -Math.floorDiv(-values.length, 8)));
    }


    public <R> R readOptional(Reader<R> reader) {
        return this.readBoolean() ? reader.apply(this) : null;
    }

    public <V> void writeOptional(V value, Writer<V> writer) {
        if (value != null) {
            this.writeBoolean(true);
            writer.accept(this, value);
        } else {
            this.writeBoolean(false);
        }
    }

    public <K, C extends Collection<K>> C readCollection(IntFunction<C> function, Reader<K> reader) {
        int size = this.readVarInt();
        C collection = function.apply(size);
        for (int i = 0; i < size; ++i) {
            collection.add(reader.apply(this));
        }
        return collection;
    }

    public <K> void writeCollection(Collection<K> collection, Writer<K> writer) {
        this.writeVarInt(collection.size());
        for (K key : collection) {
            writer.accept(this, key);
        }
    }

    public <K> List<K> readList(Reader<K> reader) {
        return this.readCollection(ArrayList::new, reader);
    }

    public <K> void writeList(List<K> list, Writer<K> writer) {
        writeVarInt(list.size());
        for (K key : list) {
            writer.accept(this, key);
        }
    }

    @FunctionalInterface
    public interface Reader<T> extends Function<PacketWrapper<?>, T> {
    }

    @FunctionalInterface
    public interface Writer<T> extends BiConsumer<PacketWrapper<?>, T>{}



}
