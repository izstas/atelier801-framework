package com.atelier801.transformice.client.proto.packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atelier801.transformice.client.proto.packet.out.OPWrapperLegacy;
import com.atelier801.transformice.client.proto.packet.out.OutboundLegacyPacket;

public final class LegacyPacketEncoder extends MessageToMessageEncoder<OutboundLegacyPacket> {
    private static final Logger logger = LoggerFactory.getLogger(LegacyPacketEncoder.class);
    private static final Joiner joiner = Joiner.on('\1');

    private final Map<Class<? extends OutboundLegacyPacket>, String> codes;

    public LegacyPacketEncoder() {
        ImmutableMap.Builder<Class<? extends OutboundLegacyPacket>, String> codesBuilder = ImmutableMap.builder();

        Reflections reflections =  ReflectionsUtil.forPackage(OutboundLegacyPacket.class.getPackage().getName());
        for (Class<? extends OutboundLegacyPacket> packet : reflections.getSubTypesOf(OutboundLegacyPacket.class)) {
            if (!packet.isAnnotationPresent(OutboundLegacyPacket.Code.class)) {
                continue;
            }

            OutboundLegacyPacket.Code packetCode = packet.getAnnotation(OutboundLegacyPacket.Code.class);

            codesBuilder.put(packet, "" + (char) packetCode.major() + (char) packetCode.minor());
            logger.debug("Packet {} has been registered with code ({}, {})",
                    packet.getSimpleName(), packetCode.major(), packetCode.minor());
        }

        codes = codesBuilder.build();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundLegacyPacket msg, List<Object> out) throws Exception {
        if (!codes.containsKey(msg.getClass())) {
            throw new EncoderException(String.format("packet %s is not registered", msg.getClass().getSimpleName()));
        }

        List<String> list = new ArrayList<>();
        list.add(codes.get(msg.getClass()));
        msg.write(list);

        out.add(new OPWrapperLegacy(joiner.join(list)));
    }
}
