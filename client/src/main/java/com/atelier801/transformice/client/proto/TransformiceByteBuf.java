package com.atelier801.transformice.client.proto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.zip.InflaterInputStream;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.WrappedByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.util.CharsetUtil;

public class TransformiceByteBuf extends WrappedByteBuf {
    private static final ThreadLocal<CharsetDecoder> utf8Decoder = ThreadLocal.withInitial(CharsetUtil.UTF_8::newDecoder);

    public TransformiceByteBuf(ByteBuf buf) {
        super(buf);
    }

    public void writeUTF(CharSequence value) {
        ensureWritable(2);
        int lengthIndex = writerIndex();
        writerIndex(lengthIndex + 2); // Shift writerIndex so we have space for the length prefix

        int length = ByteBufUtil.writeUtf8(unwrap(), value);
        if (length > 65535) {
            throw new IllegalArgumentException("can't write UTF - value requires " + length + " bytes (max: 65535)");
        }

        setShort(lengthIndex, length);
    }

    public String readUTF() {
        int length = readUnsignedShort();
        if (length == 0) {
            return "";
        }
        if (length < 0) {
            throw new DecoderException("can't read UTF - length is negative (" + length + ")");
        }

        try {
            CharBuffer result = utf8Decoder.get().decode(nioBuffer(readerIndex(), length));
            readerIndex(readerIndex() + length);

            return result.toString();
        }
        catch (IOException e) {
            throw new DecoderException("can't decode UTF", e);
        }
    }

    public String readUTFBytes(int length) {
        if (length == 0) {
            return "";
        }
        if (length < 0) {
            throw new IllegalArgumentException("length must not be negative");
        }

        try {
            CharBuffer result = utf8Decoder.get().decode(nioBuffer(readerIndex(), length));
            readerIndex(readerIndex() + length);

            return result.subSequence(0,
                    IntStream.range(0, length).filter(i -> result.charAt(i) == '\0').findFirst().orElse(length)).toString();
        }
        catch (IOException e) {
            throw new DecoderException("can't decode UTF", e);
        }
    }

    public String readCompressedUTF() {
        int length = readUnsignedShort();
        if (length == 0) {
            return "";
        }
        if (length < 0) {
            throw new DecoderException("can't read compressed UTF - length is negative (" + length + ")");
        }

        ByteBuffer data;
        try (InputStream dataIn = new InflaterInputStream(new ByteBufInputStream(unwrap(), length))) {
            data = ByteBuffer.wrap(ByteStreams.toByteArray(dataIn));
        }
        catch (IOException e) {
            throw new DecoderException("can't decompress compressed UTF", e);
        }

        try {
            return utf8Decoder.get().decode(data).toString();
        }
        catch (IOException e) {
            throw new DecoderException("can't decode UTF", e);
        }
    }

    public <T> List<T> readList(int length, Function<TransformiceByteBuf, T> reader) {
        return readList(length, () -> reader.apply(this));
    }

    public <T> List<T> readList(int length, Supplier<T> reader) {
        if (length == 0) {
            return ImmutableList.of();
        }
        if (length < 0) {
            throw new IllegalArgumentException("length must not be negative");
        }

        return ImmutableList.copyOf(Stream.generate(reader).limit(length).iterator());
    }
}
