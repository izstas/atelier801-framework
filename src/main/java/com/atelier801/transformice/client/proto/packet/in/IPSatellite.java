package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundPacket.Code(major = 44, minor = 1)
public final class IPSatellite implements InboundPacket {
    private final int key;
    private final String host;

    public IPSatellite(TransformiceByteBuf in) {
        key = in.readInt();
        host = in.readUTF();
    }

    public int getKey() {
        return key;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("key", key)
                .add("host", host)
                .toString();
    }
}
