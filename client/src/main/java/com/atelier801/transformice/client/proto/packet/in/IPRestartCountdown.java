package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;
import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.222
@InboundPacket.Code(major = 28, minor = 88)
@Getter @ToString
public final class IPRestartCountdown implements InboundPacket {
    private final int countdown;

    public IPRestartCountdown(TransformiceByteBuf in) {
        countdown = in.readInt();
    }
}
