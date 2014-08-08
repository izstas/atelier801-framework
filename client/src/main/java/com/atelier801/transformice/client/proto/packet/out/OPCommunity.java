package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundPacket.Code(major = 8, minor = 2)
public final class OPCommunity implements OutboundPacket {
    private final int community;

    public OPCommunity(int community) {
        this.community = community;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeByte(community);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("community", community)
                .toString();
    }
}
