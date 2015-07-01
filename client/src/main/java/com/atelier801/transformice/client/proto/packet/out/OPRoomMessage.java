package com.atelier801.transformice.client.proto.packet.out;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.254
@OutboundPacket.Code(major = 6, minor = 7)
@AllArgsConstructor @ToString
public final class OPRoomMessage implements OutboundPacket {
    private final String message;

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeUTF(message);
    }
}
