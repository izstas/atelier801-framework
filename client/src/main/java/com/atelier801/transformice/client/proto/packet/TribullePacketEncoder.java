package com.atelier801.transformice.client.proto.packet;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.packet.out.OPWrapperTribulle;
import com.atelier801.transformice.client.proto.packet.out.OutboundTribullePacket;

public final class TribullePacketEncoder extends MessageToMessageEncoder<OutboundTribullePacket> {
    private static final Logger logger = LoggerFactory.getLogger(TribullePacketEncoder.class);

    private final TribulleContext context;
    private final Map<Class<? extends OutboundTribullePacket>, Integer> codes;

    public TribullePacketEncoder(TribulleContext context, Function<String, Integer> labelResolver) {
        this.context = context;

        ImmutableMap.Builder<Class<? extends OutboundTribullePacket>, Integer> codesBuilder = ImmutableMap.builder();

        Reflections reflections = ReflectionsUtil.forPackage(OutboundTribullePacket.class.getPackage().getName());
        for (Class<? extends OutboundTribullePacket> packet : reflections.getSubTypesOf(OutboundTribullePacket.class)) {
            if (!packet.isAnnotationPresent(OutboundTribullePacket.Label.class)) {
                continue;
            }

            String packetLabel = packet.getAnnotation(OutboundTribullePacket.Label.class).value();

            Integer code = labelResolver.apply(packetLabel);
            if (code == null) {
                logger.warn("Packet {} has not been registered because its label {} can't be resolved",
                        packet.getSimpleName(), packetLabel);
                continue;
            }

            codesBuilder.put(packet, code);
            logger.debug("Packet {} has been registered with code {}", packet.getSimpleName(), code);
        }

        codes = codesBuilder.build();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundTribullePacket msg, List<Object> out) throws Exception {
        if (!codes.containsKey(msg.getClass())) {
            throw new EncoderException(String.format("packet %s is not registered", msg.getClass().getSimpleName()));
        }

        ByteBuf buf = ctx.alloc().buffer();
        msg.context(context).write(new TransformiceByteBuf(buf));

        out.add(new OPWrapperTribulle(codes.get(msg.getClass()), buf));
    }
}
