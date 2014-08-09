package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleChangementRang")
public final class IPTribeMemberRank implements InboundTribullePacket {
    private final int id;
    private final int rankId;

    public IPTribeMemberRank(TransformiceByteBuf in) {
        id = in.readInt();
        in.readUTF();
        rankId = in.readInt();
        in.readUTFBytes(20);
    }

    public int getId() {
        return id;
    }

    public int getRankId() {
        return rankId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("rankId", rankId)
                .toString();
    }
}
