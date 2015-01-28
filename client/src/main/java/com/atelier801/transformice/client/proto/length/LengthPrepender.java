package com.atelier801.transformice.client.proto.length;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;

public final class LengthPrepender extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        int length = msg.readableBytes() - 1; // We have to subtract 1 because the length here doesn't include fingerprint
        if (length < 256) {
            out.writeByte(1);
            out.writeByte(length);
        }
        else if (length < 65536) {
            out.writeByte(2);
            out.writeShort(length);
        }
        else if (length < 16777216) {
            out.writeByte(3);
            out.writeMedium(length);
        }
        else {
            throw new EncoderException("Frame length (" + length + ") exceeds the limit of 16777215");
        }

        out.writeBytes(msg);
    }
}
