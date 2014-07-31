package com.atelier801.transformice.client.proto.fingerprint;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class FingerprintGenerator {
    private int counter;

    public void init(int counter) {
        this.counter = counter;
    }

    public ByteBuf next(ByteBufAllocator alloc) {
        ByteBuf buf = alloc.buffer(1, 1).writeByte(counter);
        counter = (counter + 1) % 100;
        return buf;
    }
}
