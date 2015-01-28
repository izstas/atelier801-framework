package com.atelier801.transformice.client.proto.length;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

public final class LengthFrameDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 2) {
            return;
        }

        int lengthLength = in.getByte(in.readerIndex());
        if (in.readableBytes() < lengthLength) {
            return;
        }

        int length;
        switch (lengthLength) {
            case 1:
                length = in.getUnsignedByte(in.readerIndex() + 1);
                break;

            case 2:
                length = in.getUnsignedShort(in.readerIndex() + 1);
                break;

            case 3:
                length = in.getUnsignedMedium(in.readerIndex() + 1);
                break;

            default:
                throw new DecoderException("Unsupported frame length length: " + lengthLength);
        }

        if (in.readableBytes() < 1 + lengthLength + length) {
            return;
        }

        in.skipBytes(1 + lengthLength);
        ByteBuf frame = ctx.alloc().buffer(length);
        in.readBytes(frame, length);
        out.add(frame);
    }
}
