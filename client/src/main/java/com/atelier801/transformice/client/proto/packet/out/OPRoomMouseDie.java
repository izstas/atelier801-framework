package com.atelier801.transformice.client.proto.packet.out;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.252
@OutboundPacket.Code(major = 4, minor = 5)
@AllArgsConstructor @ToString
public final class OPRoomMouseDie implements OutboundPacket {
    private final int round;
    private final int type;

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(round);
        out.writeByte(type);
    }
}
