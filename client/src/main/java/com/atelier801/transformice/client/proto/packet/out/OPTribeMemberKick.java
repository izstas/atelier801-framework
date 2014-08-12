package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundTribullePacket.Label("ST_ExclureMembre")
public final class OPTribeMemberKick extends OutboundReferencedTribullePacket {
    private final int id;

    public OPTribeMemberKick(int id) {
        this.id = id;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(referenceId);
        out.writeInt(0);
        out.writeInt(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
