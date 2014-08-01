package com.atelier801.transformice.client.proto.fingerprint;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FingerprintInitPacketHandler extends SimpleChannelInboundHandler<FingerprintInitPacket> {
    private static final Logger logger = LoggerFactory.getLogger(FingerprintInitPacketHandler.class);

    private final FingerprintGenerator generator;

    public FingerprintInitPacketHandler(FingerprintGenerator generator) {
        super(false);
        this.generator = generator;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FingerprintInitPacket msg) throws Exception {
        generator.init(msg.getFingerprintCounter());
        logger.debug("Fingerprint generator has been initialized with counter = {}", msg.getFingerprintCounter());

        ctx.fireChannelRead(msg);
    }
}
