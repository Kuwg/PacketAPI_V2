package kuwg.packetapi.mojang;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

@SuppressWarnings({"deprecation", "unused"})
public class PacketDataSerializer extends ByteBuf {
    private final ByteBuf byteBuf;
    public PacketDataSerializer(ByteBuf bytebuf) {
        this.byteBuf = bytebuf;
    }

    @Override
    public int capacity() {
        return this.byteBuf.capacity();
    }

    @Override
    public ByteBuf capacity(int i) {
        return this.byteBuf.capacity(i);
    }

    @Override
    public int maxCapacity() {
        return this.byteBuf.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.byteBuf.alloc();
    }

    @Override
    public ByteOrder order() {
        return this.byteBuf.order();
    }

    @Override
    public ByteBuf order(ByteOrder byteorder) {
        return this.byteBuf.order(byteorder);
    }

    @Override
    public ByteBuf unwrap() {
        return this.byteBuf.unwrap();
    }

    @Override
    public boolean isDirect() {
        return this.byteBuf.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return this.byteBuf.isReadOnly();
    }

    @Override
    public ByteBuf asReadOnly() {
        return this.byteBuf.asReadOnly();
    }

    @Override
    public int readerIndex() {
        return this.byteBuf.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int i) {
        return this.byteBuf.readerIndex(i);
    }

    @Override
    public int writerIndex() {
        return this.byteBuf.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int i) {
        return this.byteBuf.writerIndex(i);
    }

    @Override
    public ByteBuf setIndex(int i, int j) {
        return this.byteBuf.setIndex(i, j);
    }

    @Override
    public int readableBytes() {
        return this.byteBuf.readableBytes();
    }

    @Override
    public int writableBytes() {
        return this.byteBuf.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return this.byteBuf.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return this.byteBuf.isReadable();
    }

    @Override
    public boolean isReadable(int i) {
        return this.byteBuf.isReadable(i);
    }

    @Override
    public boolean isWritable() {
        return this.byteBuf.isWritable();
    }

    @Override
    public boolean isWritable(int i) {
        return this.byteBuf.isWritable(i);
    }

    @Override
    public ByteBuf clear() {
        return this.byteBuf.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return this.byteBuf.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return this.byteBuf.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return this.byteBuf.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return this.byteBuf.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return this.byteBuf.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return this.byteBuf.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int i) {
        return this.byteBuf.ensureWritable(i);
    }

    @Override
    public int ensureWritable(int i, boolean flag) {
        return this.byteBuf.ensureWritable(i, flag);
    }

    @Override
    public boolean getBoolean(int i) {
        return this.byteBuf.getBoolean(i);
    }

    @Override
    public byte getByte(int i) {
        return this.byteBuf.getByte(i);
    }

    @Override
    public short getUnsignedByte(int i) {
        return this.byteBuf.getUnsignedByte(i);
    }

    @Override
    public short getShort(int i) {
        return this.byteBuf.getShort(i);
    }

    @Override
    public short getShortLE(int i) {
        return 0;
    }

    @Override
    public int getUnsignedShort(int i) {
        return this.byteBuf.getUnsignedShort(i);
    }

    @Override
    public int getUnsignedShortLE(int i) {
        return 0;
    }

    @Override
    public int getMedium(int i) {
        return this.byteBuf.getMedium(i);
    }

    @Override
    public int getMediumLE(int i) {
        return 0;
    }

    @Override
    public int getUnsignedMedium(int i) {
        return this.byteBuf.getUnsignedMedium(i);
    }

    @Override
    public int getUnsignedMediumLE(int i) {
        return 0;
    }

    @Override
    public int getInt(int i) {
        return this.byteBuf.getInt(i);
    }

    @Override
    public int getIntLE(int i) {
        return 0;
    }

    @Override
    public long getUnsignedInt(int i) {
        return this.byteBuf.getUnsignedInt(i);
    }

    @Override
    public long getUnsignedIntLE(int i) {
        return 0;
    }

    @Override
    public long getLong(int i) {
        return this.byteBuf.getLong(i);
    }

    @Override
    public long getLongLE(int i) {
        return 0;
    }

    @Override
    public char getChar(int i) {
        return this.byteBuf.getChar(i);
    }

    @Override
    public float getFloat(int i) {
        return this.byteBuf.getFloat(i);
    }

    @Override
    public double getDouble(int i) {
        return this.byteBuf.getDouble(i);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf bytebuf) {
        return this.byteBuf.getBytes(i, bytebuf);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf bytebuf, int j) {
        return this.byteBuf.getBytes(i, bytebuf, j);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf bytebuf, int j, int k) {
        return this.byteBuf.getBytes(i, bytebuf, j, k);
    }

    @Override
    public ByteBuf getBytes(int i, byte[] abyte) {
        return this.byteBuf.getBytes(i, abyte);
    }

    @Override
    public ByteBuf getBytes(int i, byte[] abyte, int j, int k) {
        return this.byteBuf.getBytes(i, abyte, j, k);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuffer bytebuffer) {
        return this.byteBuf.getBytes(i, bytebuffer);
    }

    @Override
    public ByteBuf getBytes(int i, OutputStream outputstream, int j) throws IOException {
        return this.byteBuf.getBytes(i, outputstream, j);
    }

    @Override
    public int getBytes(int i, GatheringByteChannel gatheringbytechannel, int j) throws IOException {
        return this.byteBuf.getBytes(i, gatheringbytechannel, j);
    }

    @Override
    public int getBytes(int i, FileChannel fileChannel, long l, int i1) {
        return 0;
    }

    @Override
    public CharSequence getCharSequence(int i, int i1, Charset charset) {
        return null;
    }

    @Override
    public ByteBuf setBoolean(int i, boolean flag) {
        return this.byteBuf.setBoolean(i, flag);
    }

    @Override
    public ByteBuf setByte(int i, int j) {
        return this.byteBuf.setByte(i, j);
    }

    @Override
    public ByteBuf setShort(int i, int j) {
        return this.byteBuf.setShort(i, j);
    }

    @Override
    public ByteBuf setShortLE(int i, int i1) {
        return null;
    }
    @Override
    public ByteBuf setMedium(int i, int j) {
        return this.byteBuf.setMedium(i, j);
    }

    @Override
    public ByteBuf setMediumLE(int i, int i1) {
        return null;
    }

    @Override
    public ByteBuf setInt(int i, int j) {
        return this.byteBuf.setInt(i, j);
    }

    @Override
    public ByteBuf setIntLE(int i, int i1) {
        return null;
    }

    @Override
    public ByteBuf setLong(int i, long j) {
        return this.byteBuf.setLong(i, j);
    }

    @Override
    public ByteBuf setLongLE(int i, long l) {
        return null;
    }

    @Override
    public ByteBuf setChar(int i, int j) {
        return this.byteBuf.setChar(i, j);
    }

    @Override
    public ByteBuf setFloat(int i, float f) {
        return this.byteBuf.setFloat(i, f);
    }

    @Override
    public ByteBuf setDouble(int i, double d0) {
        return this.byteBuf.setDouble(i, d0);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf bytebuf) {
        return this.byteBuf.setBytes(i, bytebuf);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf bytebuf, int j) {
        return this.byteBuf.setBytes(i, bytebuf, j);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf bytebuf, int j, int k) {
        return this.byteBuf.setBytes(i, bytebuf, j, k);
    }

    @Override
    public ByteBuf setBytes(int i, byte[] abyte) {
        return this.byteBuf.setBytes(i, abyte);
    }

    @Override
    public ByteBuf setBytes(int i, byte[] abyte, int j, int k) {
        return this.byteBuf.setBytes(i, abyte, j, k);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuffer bytebuffer) {
        return this.byteBuf.setBytes(i, bytebuffer);
    }

    @Override
    public int setBytes(int i, InputStream inputstream, int j) throws IOException {
        return this.byteBuf.setBytes(i, inputstream, j);
    }

    @Override
    public int setBytes(int i, ScatteringByteChannel scatteringbytechannel, int j) throws IOException {
        return this.byteBuf.setBytes(i, scatteringbytechannel, j);
    }

    @Override
    public int setBytes(int i, FileChannel fileChannel, long l, int i1) {
        return 0;
    }

    @Override
    public ByteBuf setZero(int i, int j) {
        return this.byteBuf.setZero(i, j);
    }

    @Override
    public int setCharSequence(int i, CharSequence charSequence, Charset charset) {
        return 0;
    }

    @Override
    public boolean readBoolean() {
        return this.byteBuf.readBoolean();
    }

    @Override
    public byte readByte() {
        return this.byteBuf.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return this.byteBuf.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return this.byteBuf.readShort();
    }

    @Override
    public short readShortLE() {
        return 0;
    }

    @Override
    public int readUnsignedShort() {
        return this.byteBuf.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return 0;
    }

    @Override
    public int readMedium() {
        return this.byteBuf.readMedium();
    }
    public static int calculateVarIntSize(int value) {
        for (int size = 1; size < 5; ++size) {
            if ((value & -1 << size * 7) == 0) {
                return size;
            }
        }

        return 5;
    }

    @Override
    public int readMediumLE() {
        return 0;
    }

    @Override
    public int readUnsignedMedium() {
        return this.byteBuf.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return 0;
    }

    @Override
    public int readInt() {
        return this.byteBuf.readInt();
    }
    public int readVarInt() {
        int i = 0;
        int j = 0;

        byte b0;
        do {
            b0 = this.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while((b0 & 128) == 128);

        return i;
    }
    @Override
    public int readIntLE() {
        return 0;
    }

    @Override
    public long readUnsignedInt() {
        return this.byteBuf.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return 0;
    }

    @Override
    public long readLong() {
        return this.byteBuf.readLong();
    }

    @Override
    public long readLongLE() {
        return 0;
    }

    @Override
    public char readChar() {
        return this.byteBuf.readChar();
    }

    @Override
    public float readFloat() {
        return this.byteBuf.readFloat();
    }

    @Override
    public double readDouble() {
        return this.byteBuf.readDouble();
    }

    @Override
    public ByteBuf readBytes(int i) {
        return this.byteBuf.readBytes(i);
    }

    @Override
    public ByteBuf readSlice(int i) {
        return this.byteBuf.readSlice(i);
    }

    @Override
    public ByteBuf readRetainedSlice(int i) {
        return null;
    }

    @Override
    public ByteBuf readBytes(ByteBuf bytebuf) {
        return this.byteBuf.readBytes(bytebuf);
    }

    @Override
    public ByteBuf readBytes(ByteBuf bytebuf, int i) {
        return this.byteBuf.readBytes(bytebuf, i);
    }

    @Override
    public ByteBuf readBytes(ByteBuf bytebuf, int i, int j) {
        return this.byteBuf.readBytes(bytebuf, i, j);
    }

    @Override
    public ByteBuf readBytes(byte[] abyte) {
        return this.byteBuf.readBytes(abyte);
    }

    @Override
    public ByteBuf readBytes(byte[] abyte, int i, int j) {
        return this.byteBuf.readBytes(abyte, i, j);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer bytebuffer) {
        return this.byteBuf.readBytes(bytebuffer);
    }

    @Override
    public ByteBuf readBytes(OutputStream outputstream, int i) throws IOException {
        return this.byteBuf.readBytes(outputstream, i);
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringbytechannel, int i) throws IOException {
        return this.byteBuf.readBytes(gatheringbytechannel, i);
    }

    @Override
    public CharSequence readCharSequence(int i, Charset charset) {
        return null;
    }

    @Override
    public int readBytes(FileChannel fileChannel, long l, int i) {
        return 0;
    }

    @Override
    public ByteBuf skipBytes(int i) {
        return this.byteBuf.skipBytes(i);
    }

    @Override
    public ByteBuf writeBoolean(boolean flag) {
        return this.byteBuf.writeBoolean(flag);
    }

    @Override
    public ByteBuf writeByte(int i) {
        return this.byteBuf.writeByte(i);
    }

    @Override
    public ByteBuf writeShort(int i) {
        return this.byteBuf.writeShort(i);
    }

    @Override
    public ByteBuf writeShortLE(int i) {
        return null;
    }

    @Override
    public ByteBuf writeMedium(int i) {
        return this.byteBuf.writeMedium(i);
    }

    @Override
    public ByteBuf writeMediumLE(int i) {
        return null;
    }

    @Override
    public ByteBuf writeInt(int i) {
        return this.byteBuf.writeInt(i);
    }

    @Override
    public ByteBuf writeIntLE(int i) {
        return null;
    }

    @Override
    public ByteBuf writeLong(long i) {
        return this.byteBuf.writeLong(i);
    }

    @Override
    public ByteBuf writeLongLE(long l) {
        return null;
    }

    @Override
    public ByteBuf writeChar(int i) {
        return this.byteBuf.writeChar(i);
    }

    @Override
    public ByteBuf writeFloat(float f) {
        return this.byteBuf.writeFloat(f);
    }

    @Override
    public ByteBuf writeDouble(double d0) {
        return this.byteBuf.writeDouble(d0);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf bytebuf) {
        return this.byteBuf.writeBytes(bytebuf);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf bytebuf, int i) {
        return this.byteBuf.writeBytes(bytebuf, i);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf bytebuf, int i, int j) {
        return this.byteBuf.writeBytes(bytebuf, i, j);
    }

    @Override
    public ByteBuf writeBytes(byte[] abyte) {
        return this.byteBuf.writeBytes(abyte);
    }

    @Override
    public ByteBuf writeBytes(byte[] abyte, int i, int j) {
        return this.byteBuf.writeBytes(abyte, i, j);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer bytebuffer) {
        return this.byteBuf.writeBytes(bytebuffer);
    }

    @Override
    public int writeBytes(InputStream inputstream, int i) throws IOException {
        return this.byteBuf.writeBytes(inputstream, i);
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringbytechannel, int i) throws IOException {
        return this.byteBuf.writeBytes(scatteringbytechannel, i);
    }

    @Override
    public int writeBytes(FileChannel fileChannel, long l, int i) {
        return 0;
    }

    @Override
    public ByteBuf writeZero(int i) {
        return this.byteBuf.writeZero(i);
    }

    @Override
    public int writeCharSequence(CharSequence charsequence, Charset charset) {
        return this.byteBuf.writeCharSequence(charsequence, charset);
    }

    @Override
    public int indexOf(int i, int j, byte b) {
        return this.byteBuf.indexOf(i, j, b);
    }

    @Override
    public int bytesBefore(byte b) {
        return this.byteBuf.bytesBefore(b);
    }

    @Override
    public int bytesBefore(int i, byte b) {
        return this.byteBuf.bytesBefore(i, b);
    }

    @Override
    public int bytesBefore(int i, int j, byte b) {
        return this.byteBuf.bytesBefore(i, j, b);
    }

    @Override
    public int forEachByte(ByteProcessor byteProcessor) {
        return 0;
    }

    @Override
    public int forEachByte(int i, int i1, ByteProcessor byteProcessor) {
        return 0;
    }

    @Override
    public int forEachByteDesc(ByteProcessor byteProcessor) {
        return 0;
    }

    @Override
    public int forEachByteDesc(int i, int i1, ByteProcessor byteProcessor) {
        return 0;
    }

    @Override
    public ByteBuf copy() {
        return this.byteBuf.copy();
    }

    @Override
    public ByteBuf copy(int i, int j) {
        return this.byteBuf.copy(i, j);
    }

    @Override
    public ByteBuf slice() {
        return this.byteBuf.slice();
    }

    @Override
    public ByteBuf retainedSlice() {
        return null;
    }

    @Override
    public ByteBuf slice(int i, int j) {
        return this.byteBuf.slice(i, j);
    }

    @Override
    public ByteBuf retainedSlice(int i, int i1) {
        return null;
    }

    @Override
    public ByteBuf duplicate() {
        return this.byteBuf.duplicate();
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return null;
    }

    @Override
    public int nioBufferCount() {
        return this.byteBuf.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return this.byteBuf.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int i, int j) {
        return this.byteBuf.nioBuffer(i, j);
    }

    @Override
    public ByteBuffer internalNioBuffer(int i, int j) {
        return this.byteBuf.internalNioBuffer(i, j);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.byteBuf.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int i, int j) {
        return this.byteBuf.nioBuffers(i, j);
    }

    @Override
    public boolean hasArray() {
        return this.byteBuf.hasArray();
    }

    @Override
    public byte[] array() {
        return this.byteBuf.array();
    }

    @Override
    public int arrayOffset() {
        return this.byteBuf.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.byteBuf.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return this.byteBuf.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {
        return this.byteBuf.toString(charset);
    }

    @Override
    public String toString(int i, int j, Charset charset) {
        return this.byteBuf.toString(i, j, charset);
    }

    @Override
    public int hashCode() {
        return this.byteBuf.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ByteBuf)&&this.byteBuf==o;
    }


    @Override
    public int compareTo(ByteBuf bytebuf) {
        return this.byteBuf.compareTo(bytebuf);
    }

    @Override
    public String toString() {
        return this.byteBuf.toString();
    }

    @Override
    public ByteBuf retain(int i) {
        return this.byteBuf.retain(i);
    }

    @Override
    public ByteBuf retain() {
        return this.byteBuf.retain();
    }

    @Override
    public ByteBuf touch() {
        return null;
    }

    @Override
    public ByteBuf touch(Object o) {
        return null;
    }

    @Override
    public int refCnt() {
        return this.byteBuf.refCnt();
    }

    @Override
    public boolean release() {
        return this.byteBuf.release();
    }

    @Override
    public boolean release(int i) {
        return this.byteBuf.release(i);
    }
    public void writeVarInt(int i) {
        while((i & -128) != 0) {
            this.writeByte(i & 127 | 128);
            i >>>= 7;
        }

        this.writeByte(i);
    }

    public ByteBuf asByteBuf() {
        return byteBuf;
    }
}