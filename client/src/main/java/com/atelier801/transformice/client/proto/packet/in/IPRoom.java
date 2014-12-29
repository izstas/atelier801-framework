package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;
import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.215
@InboundPacket.Code(major = 5, minor = 21)
@Getter @ToString
public final class IPRoom implements InboundPacket {
    private final String room;

    public IPRoom(TransformiceByteBuf in) {
        in.readBoolean();
        room = in.readUTF();
    }
}
