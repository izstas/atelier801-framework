package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.196
@OutboundTribullePacket.Label("ST_DemandeMembresTribu")
public final class OPTribeMembersRequest extends OutboundReferencedTribullePacket {
    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(referenceId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }
}
