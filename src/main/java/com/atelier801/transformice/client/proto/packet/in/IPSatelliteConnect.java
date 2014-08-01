package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.fingerprint.FingerprintInitPacket;

// Valid for 1.180
@InboundPacket.Code(major = 44, minor = 22)
public final class IPSatelliteConnect implements InboundPacket, FingerprintInitPacket {
    private final int fingerprintCounter;

    public IPSatelliteConnect(TransformiceByteBuf in) {
        fingerprintCounter = in.readByte();
    }

    @Override
    public int getFingerprintCounter() {
        return fingerprintCounter;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("fingerprintCounter", fingerprintCounter)
                .toString();
    }
}
