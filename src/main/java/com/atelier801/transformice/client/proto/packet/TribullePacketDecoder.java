package com.atelier801.transformice.client.proto.packet;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import com.google.common.collect.ImmutableMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.packet.in.IPWrapperTribulle;
import com.atelier801.transformice.client.proto.packet.in.InboundTribullePacket;

public final class TribullePacketDecoder extends MessageToMessageDecoder<IPWrapperTribulle> {
    private static final Logger logger = LoggerFactory.getLogger(TribullePacketDecoder.class);

    private final TribulleContext context;
    private final Map<Integer, Function<TransformiceByteBuf, InboundTribullePacket>> packets;

    public TribullePacketDecoder(TribulleContext context, Function<String, Integer> labelResolver) {
        this.context = context;

        ImmutableMap.Builder<Integer, Function<TransformiceByteBuf, InboundTribullePacket>> packetsBuilder =
                ImmutableMap.builder();

        Reflections reflections = ReflectionsUtil.forPackage(InboundTribullePacket.class.getPackage().getName());
        for (Class<? extends InboundTribullePacket> packet : reflections.getSubTypesOf(InboundTribullePacket.class)) {
            if (!packet.isAnnotationPresent(InboundTribullePacket.Label.class)) {
                continue;
            }

            String packetLabel = packet.getAnnotation(InboundTribullePacket.Label.class).value();

            Integer code = labelResolver.apply(packetLabel);
            if (code == null) {
                logger.warn("Packet {} has not been registered because its label {} can't be resolved",
                        packet.getSimpleName(), packetLabel);
                continue;
            }

            Constructor<? extends InboundTribullePacket> packetConstructor;
            try {
                packetConstructor = packet.getConstructor(TransformiceByteBuf.class);
            }
            catch (NoSuchMethodException e) {
                logger.warn("Packet {} has not been registered because its class doesn't have required constructor",
                        packet.getSimpleName());
                continue;
            }

            packetsBuilder.put(code, buf -> {
                try {
                    return packetConstructor.newInstance(buf);
                }
                catch (ReflectiveOperationException e) {
                    throw new DecoderException("can't instantiate packet class", e);
                }
            });

            logger.debug("Packet {} has been registered with code {}", packet.getSimpleName(), code);
        }

        packets = packetsBuilder.build();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, IPWrapperTribulle msg, List<Object> out) throws Exception {
        if (!packets.containsKey(msg.getCode())) {
            logger.trace("Packet code {} is not known; packet has been discarded ({} bytes)",
                    msg.getCode(), msg.getBody().readableBytes());
            return;
        }

        out.add(packets.get(msg.getCode()).apply(new TransformiceByteBuf(msg.getBody())).context(context));
    }
}
