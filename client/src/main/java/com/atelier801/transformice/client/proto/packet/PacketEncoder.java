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
import com.atelier801.transformice.client.proto.packet.out.OutboundPacket;

public final class PacketEncoder extends MessageToMessageEncoder<OutboundPacket> {
    private static final Logger logger = LoggerFactory.getLogger(PacketEncoder.class);

    private final Map<Class<? extends OutboundPacket>, Code> codes;
    private final Function<Integer, Integer> codeDynamicTransformer;

    public PacketEncoder(Function<Integer, Integer> codePreTransformer,
                         Function<Integer, Integer> codeDynamicTransformer) {
        this.codeDynamicTransformer = codeDynamicTransformer;

        ImmutableMap.Builder<Class<? extends OutboundPacket>, Code> codesBuilder = ImmutableMap.builder();

        Reflections reflections = ReflectionsUtil.forPackage(OutboundPacket.class.getPackage().getName());
        for (Class<? extends OutboundPacket> packet : reflections.getSubTypesOf(OutboundPacket.class)) {
            if (!packet.isAnnotationPresent(OutboundPacket.Code.class)) {
                continue;
            }

            OutboundPacket.Code packetCode = packet.getAnnotation(OutboundPacket.Code.class);
            int major = packetCode.major();
            int minor = packetCode.minor();

            if (packetCode.transformable()) {
                Integer transformedMajor = codePreTransformer.apply(major);
                Integer transformedMinor = codePreTransformer.apply(minor);

                if (transformedMajor == null || transformedMinor == null) {
                    logger.warn("Packet {} has not been registered because its code ({}, {}) can't be transformed",
                            packet.getSimpleName(), major, minor);
                    continue;
                }

                major = transformedMajor;
                minor = transformedMinor;
            }

            codesBuilder.put(packet, new Code(major, minor, packetCode.transformable()));
            logger.debug("Packet {} has been registered with code ({}, {})",
                    packet.getSimpleName(), major, minor);
        }

        codes = codesBuilder.build();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundPacket msg, List<Object> out) throws Exception {
        Code code = codes.get(msg.getClass());
        if (code == null) {
            throw new EncoderException(String.format("Packet %s is not registered", msg.getClass().getSimpleName()));
        }

        ByteBuf buf = ctx.alloc().buffer();
        if (code.transformable) {
            buf.writeByte(codeDynamicTransformer.apply(code.major));
            buf.writeByte(codeDynamicTransformer.apply(code.minor));
        }
        else {
            buf.writeByte(code.major);
            buf.writeByte(code.minor);
        }
        msg.write(new TransformiceByteBuf(buf));

        out.add(buf);
    }


    private static final class Code {
        final int major;
        final int minor;
        final boolean transformable;

        Code(int major, int minor, boolean transformable) {
            this.major = major;
            this.minor = minor;
            this.transformable = transformable;
        }
    }
}
