package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundPacket.Code(major = 5, minor = 21)
public final class IPRoom implements InboundPacket {
    private final String room;

    public IPRoom(TransformiceByteBuf in) {
        room = in.readUTF();
    }

    public String getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("room", room)
                .toString();
    }
}
