package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.252
@InboundPacket.Code(major = 8, minor = 6)
@Getter @ToString
public final class IPRoomMouseHole implements InboundPacket {
    private final int mouseId;
    private final int mouseNewScore;
    private final int position;
    private final int time;

    public IPRoomMouseHole(TransformiceByteBuf in) {
        in.readByte();
        mouseId = in.readInt();
        mouseNewScore = in.readShort();
        position = in.readByte();
        time = in.readUnsignedShort();
    }
}
