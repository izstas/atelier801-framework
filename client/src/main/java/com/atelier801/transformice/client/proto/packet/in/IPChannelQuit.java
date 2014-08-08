package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleQuitteCanal")
public final class IPChannelQuit implements InboundTribullePacket {
    private final int id;

    public IPChannelQuit(TransformiceByteBuf in) {
        id = in.readInt();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
