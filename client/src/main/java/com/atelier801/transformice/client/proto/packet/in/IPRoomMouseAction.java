package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.252
@InboundPacket.Code(major = 8, minor = 1)
@Getter @ToString
public final class IPRoomMouseAction implements InboundPacket {
    private final int mouseId;
    private final int action;
    private final String actionData;

    public IPRoomMouseAction(TransformiceByteBuf in) {
        mouseId = in.readInt();
        action = in.readByte();
        // TFM explicitly checks if the action equals 10, I'm trying a different approach
        actionData = in.isReadable() ? in.readUTF() : null;
    }
}
