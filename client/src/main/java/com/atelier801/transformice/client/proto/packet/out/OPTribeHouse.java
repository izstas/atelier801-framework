package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundPacket.Code(major = 16, minor = 1)
public final class OPTribeHouse implements OutboundPacket {
    @Override
    public void write(TransformiceByteBuf out) {
        // Empty
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }
}
