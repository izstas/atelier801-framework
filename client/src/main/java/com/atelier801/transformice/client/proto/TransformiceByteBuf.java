package com.atelier801.transformice.client.proto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.zip.InflaterInputStream;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Bytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.WrappedByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.util.CharsetUtil;

public class TransformiceByteBuf extends WrappedByteBuf {
    public TransformiceByteBuf(ByteBuf buf) {
        super(buf);
    }

    public void writeUTF(String value) {
        byte[] data = value.getBytes(CharsetUtil.UTF_8);
        if (data.length > 65535) {
            throw new IllegalArgumentException(String.format("value requires %d bytes (max: 65535)", data.length));
        }

        writeShort(data.length);
        writeBytes(data);
    }

    public String readUTF() {
        byte[] data = new byte[readUnsignedShort()];
        readBytes(data);

        return new String(data, CharsetUtil.UTF_8);
    }

    public String readUTFBytes(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must not be negative");
        }

        byte[] data = new byte[length];
        readBytes(data);

        return new String(data, 0, Bytes.indexOf(data, (byte) 0), CharsetUtil.UTF_8);
    }

    public String readCompressedUTF() {
        int length = readUnsignedShort();
        if (length == 0) {
            return "";
        }
        if (length < 0) {
            throw new DecoderException("can't read compressed UTF - length is negative (" + length + ")");
        }

        byte[] data;
        try (InputStream dataIn = new InflaterInputStream(new ByteBufInputStream(this, length))) {
            data = ByteStreams.toByteArray(dataIn);
        }
        catch (IOException e) {
            throw new DecoderException("can't decompress compressed UTF", e);
        }

        return new String(data, CharsetUtil.UTF_8);
    }

    public <T> List<T> readList(int length, Function<TransformiceByteBuf, T> reader) {
        return readList(length, () -> reader.apply(this));
    }

    public <T> List<T> readList(int length, Supplier<T> reader) {
        if (length < 0) {
            throw new IllegalArgumentException("length must not be negative");
        }

        if (length == 0) {
            return ImmutableList.of();
        }

        ImmutableList.Builder<T> builder = ImmutableList.builder();
        for (int i = 0; i < length; i++) {
            builder.add(reader.get());
        }

        return builder.build();
    }
}
