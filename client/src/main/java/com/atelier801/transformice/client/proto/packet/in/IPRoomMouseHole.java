package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.212
@InboundPacket.Code(major = 8, minor = 6)
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
        time = in.readShort();
    }

    public int getMouseId() {
        return mouseId;
    }

    public int getMouseNewScore() {
        return mouseNewScore;
    }

    public int getPosition() {
        return position;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mouseId", mouseId)
                .add("mouseNewScore", mouseNewScore)
                .add("position", position)
                .add("time", time)
                .toString();
    }
}
