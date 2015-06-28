package com.atelier801.transformice.client.proto.packet.out;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.252
@OutboundPacket.Code(major = 4, minor = 9)
@AllArgsConstructor @ToString
public final class OPRoomMouseAction implements OutboundPacket {
    private final int action;

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeByte(action);
    }
}
