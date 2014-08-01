package com.atelier801.transformice.client.proto.fingerprint;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public final class FingerprintPrepender extends MessageToMessageEncoder<ByteBuf> {
    private final FingerprintGenerator generator;

    public FingerprintPrepender(FingerprintGenerator generator) {
        this.generator = generator;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        ByteBuf fingerprint = generator.next(ctx.alloc());

        out.add(ctx.alloc().compositeBuffer(2).addComponents(fingerprint, msg.retain())
                .writerIndex(fingerprint.readableBytes() + msg.readableBytes()));
    }
}
