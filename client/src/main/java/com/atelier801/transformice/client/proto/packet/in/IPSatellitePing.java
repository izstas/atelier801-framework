package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.190
@InboundPacket.Code(major = 28, minor = 6)
public final class IPSatellitePing implements InboundPacket {
    private final int counter;

    public IPSatellitePing(TransformiceByteBuf in) {
        counter = in.readByte();
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("counter", counter)
                .toString();
    }
}
