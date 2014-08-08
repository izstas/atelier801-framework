package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleDepartMembre")
public final class IPTribeMemberLeave implements InboundTribullePacket {
    private final int id;

    public IPTribeMemberLeave(TransformiceByteBuf in) {
        id = in.readInt();
        in.readUTF();
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
