package com.atelier801.transformice.client.proto;

import java.util.List;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableList;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.WrappedByteBuf;
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
