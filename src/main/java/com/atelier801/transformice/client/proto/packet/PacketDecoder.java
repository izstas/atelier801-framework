package com.atelier801.transformice.client.proto.packet;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Function;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.packet.in.InboundPacket;

public final class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static final Logger logger = LoggerFactory.getLogger(PacketDecoder.class);

    private final Table<Integer, Integer, Function<TransformiceByteBuf, InboundPacket>> packets;

    public PacketDecoder() {
        ImmutableTable.Builder<Integer, Integer, Function<TransformiceByteBuf, InboundPacket>> packetsBuilder =
                ImmutableTable.builder();

        Reflections reflections = ReflectionsUtil.forPackage(InboundPacket.class.getPackage().getName());
        for (Class<? extends InboundPacket> packet : reflections.getSubTypesOf(InboundPacket.class)) {
            if (!packet.isAnnotationPresent(InboundPacket.Code.class)) {
                continue;
            }

            InboundPacket.Code packetCode = packet.getAnnotation(InboundPacket.Code.class);

            final Constructor<? extends InboundPacket> packetConstructor;
            try {
                packetConstructor = packet.getConstructor(TransformiceByteBuf.class);
            }
            catch (NoSuchMethodException e) {
                logger.warn("Packet {} has not been registered because its class doesn't have required constructor",
                        packet.getSimpleName());
                continue;
            }

            packetsBuilder.put(packetCode.major(), packetCode.minor(), buf -> {
                try {
                    return packetConstructor.newInstance(buf);
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
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int majorCode = msg.readUnsignedByte();
        int minorCode = msg.readUnsignedByte();

        if (!packets.contains(majorCode, minorCode)) {
            logger.trace("Packet code ({}, {}) is not known; packet has been discarded ({} bytes)",
                    majorCode, minorCode, msg.readableBytes());
            return;
        }

        out.add(packets.get(majorCode, minorCode).apply(new TransformiceByteBuf(msg)));
    }
}
