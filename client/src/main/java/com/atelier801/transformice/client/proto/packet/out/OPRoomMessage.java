package com.atelier801.transformice.client.proto.packet.out;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.255
@OutboundPacket.Code(major = 6, minor = 6)
@AllArgsConstructor @ToString
public final class OPRoomMessage implements OutboundPacket {
    private final String message;

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeUTF(message);
    }
}
