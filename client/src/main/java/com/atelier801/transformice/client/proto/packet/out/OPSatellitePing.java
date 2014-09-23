package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.190
@OutboundPacket.Code(major = 28, minor = 6)
public final class OPSatellitePing implements OutboundPacket {
    private final int counter;

    public OPSatellitePing(int counter) {
        this.counter = counter;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeByte(counter);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("counter", counter)
                .toString();
    }
}
