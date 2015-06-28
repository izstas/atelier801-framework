package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.252
@InboundPacket.Code(major = 4, minor = 9)
@Getter @ToString
public final class IPRoomMouseAction implements InboundPacket {
    private final int mouseId;
    private final int action;

    public IPRoomMouseAction(TransformiceByteBuf in) {
        mouseId = in.readInt();
        action = in.readByte();
        in.readBoolean();
    }
}
