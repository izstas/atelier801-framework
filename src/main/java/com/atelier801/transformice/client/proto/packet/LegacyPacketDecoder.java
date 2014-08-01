package com.atelier801.transformice.client.proto.packet;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atelier801.transformice.client.proto.packet.in.IPWrapperLegacy;
import com.atelier801.transformice.client.proto.packet.in.InboundLegacyPacket;

public final class LegacyPacketDecoder extends MessageToMessageDecoder<IPWrapperLegacy> {
    private static final Logger logger = LoggerFactory.getLogger(LegacyPacketDecoder.class);
    private static final Splitter splitter = Splitter.on('\1');

    private final Table<Integer, Integer, Function<List<String>, InboundLegacyPacket>> packets;

    public LegacyPacketDecoder() {
        ImmutableTable.Builder<Integer, Integer, Function<List<String>, InboundLegacyPacket>> packetsBuilder =
                ImmutableTable.builder();

        Reflections reflections = ReflectionsUtil.forPackage(InboundLegacyPacket.class.getPackage().getName());
        for (Class<? extends InboundLegacyPacket> packet : reflections.getSubTypesOf(InboundLegacyPacket.class)) {
            if (!packet.isAnnotationPresent(InboundLegacyPacket.Code.class)) {
                continue;
            }

            InboundLegacyPacket.Code packetCode = packet.getAnnotation(InboundLegacyPacket.Code.class);

            final Constructor<? extends InboundLegacyPacket> packetConstructor;
            try {
                packetConstructor = packet.getConstructor(List.class);
            }
            catch (NoSuchMethodException e) {
                logger.warn("Packet {} has not been registered because its class doesn't have required constructor",
                        packet.getSimpleName());
                continue;
            }

            packetsBuilder.put(packetCode.major(), packetCode.minor(), list -> {
                try {
                    return packetConstructor.newInstance(list);
                }
                catch (ReflectiveOperationException e) {
                    throw new DecoderException("can't instantiate packet class", e);
                }
            });

            logger.debug("Packet {} has been registered with code ({}, {})",
                    packet.getSimpleName(), packetCode.major(), packetCode.minor());
        }

        packets = packetsBuilder.build();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, IPWrapperLegacy msg, List<Object> out) throws Exception {
        List<String> list = splitter.splitToList(msg.getPayload());

        int majorCode = list.get(0).charAt(0);
        int minorCode = list.get(0).charAt(1);

        if (!packets.contains(majorCode, minorCode)) {
            logger.trace("Packet code ({}, {}) is not known; packet has been discarded ({} elements)",
                    majorCode, minorCode, list.size() - 1);
            return;
        }

        out.add(packets.get(majorCode, minorCode).apply(list.subList(1, list.size() - 1)));
    }
}
