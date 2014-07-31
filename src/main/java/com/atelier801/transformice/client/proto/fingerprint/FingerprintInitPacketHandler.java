package com.atelier801.transformice.client.proto.fingerprint;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public final class FingerprintInitPacketHandler extends SimpleChannelInboundHandler<FingerprintInitPacket> {
    private final FingerprintGenerator generator;

    public FingerprintInitPacketHandler(FingerprintGenerator generator) {
        super(false);
        this.generator = generator;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FingerprintInitPacket msg) throws Exception {
        generator.init(msg.getFingerprintCounter());

        ctx.fireChannelRead(msg);
    }
}
