package com.atelier801.transformice.client.proto;

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
}
