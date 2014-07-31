package com.atelier801.transformice.client.proto.fingerprint;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public final class FingerprintPrepender extends MessageToMessageEncoder<ByteBuf> {
    private final FingerprintGenerator generator;

    public FingerprintPrepender(FingerprintGenerator generator) {
        this.generator = generator;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(Unpooled.wrappedBuffer(generator.next(ctx.alloc()), msg.retain()));
    }
}
