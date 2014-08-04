package com.atelier801.transformice.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import com.atelier801.transformice.client.proto.fingerprint.*;
import com.atelier801.transformice.client.proto.packet.*;

final class TransformiceChannelInitializer extends ChannelInitializer<Channel> {
    private final ProtoData data;
    private final ChannelHandler[] handlers;

    TransformiceChannelInitializer(ProtoData data, ChannelHandler... handlers) {
        this.data = data;
        this.handlers = handlers;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        FingerprintGenerator fingerprintGenerator = new FingerprintGenerator();
        TribulleContext tribulleContext = new TribulleContext();

        ch.pipeline().addLast("enc-length", new LengthFieldPrepender(4, true));
        ch.pipeline().addLast("dec-length", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, -4, 4));

        ch.pipeline().addLast("enc-fingerprint", new FingerprintPrepender(fingerprintGenerator));

        ch.pipeline().addLast("enc-packet", new PacketEncoder(data.getCodeTransforms()::get));
        ch.pipeline().addLast("dec-packet", new PacketDecoder());

        ch.pipeline().addLast("enc-packet-legacy", new LegacyPacketEncoder());
        ch.pipeline().addLast("dec-packet-legacy", new LegacyPacketDecoder());

        ch.pipeline().addLast("enc-packet-tribulle",
                new TribullePacketEncoder(tribulleContext, data.getTribulleCodes()::get));
        ch.pipeline().addLast("dec-packet-tribulle",
                new TribullePacketDecoder(tribulleContext, data.getTribulleCodes()::get));

        ch.pipeline().addLast("handler-logging", new LoggingHandler(TransformiceClient.class, LogLevel.TRACE));
        ch.pipeline().addLast("handler-fingerprint", new FingerprintInitPacketHandler(fingerprintGenerator));
        ch.pipeline().addLast(handlers);
    }
}
