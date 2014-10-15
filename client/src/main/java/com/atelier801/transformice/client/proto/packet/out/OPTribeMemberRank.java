package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.196
@OutboundTribullePacket.Label("ST_AffecterRang")
public final class OPTribeMemberRank extends OutboundReferencedTribullePacket {
    private final int id;
    private final int rankId;

    public OPTribeMemberRank(int id, int rankId) {
        this.id = id;
        this.rankId = rankId;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(referenceId);
        out.writeInt(id);
        out.writeInt(rankId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("rankId", rankId)
                .toString();
    }
}
