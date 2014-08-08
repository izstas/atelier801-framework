package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleExclusion")
public final class IPTribeMemberKick implements InboundTribullePacket {
    private final String kicker;
    private final int id;

    public IPTribeMemberKick(TransformiceByteBuf in) {
        kicker = in.readUTF();
        id = in.readInt();
        in.readUTF();
    }

    public String getKicker() {
        return kicker;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("kicker", kicker)
                .add("id", id)
                .toString();
    }
}
