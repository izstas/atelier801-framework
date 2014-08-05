package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundTribullePacket.Label("ST_DemandeMembresTribu")
public final class OPTribeMembersRequest extends OutboundReferencedTribullePacket {
    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(referenceId);
        out.writeInt(0);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).toString();
    }
}
