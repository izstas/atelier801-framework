package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundPacket.Code(major = 44, minor = 1)
public final class OPSatelliteConnect implements OutboundPacket {
    private final int key;

    public OPSatelliteConnect(int key) {
        this.key = key;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(key);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key)
                .toString();
    }
}
