package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.fingerprint.FingerprintInitPacket;

// Valid for 1.180
@InboundPacket.Code(major = 26, minor = 3)
public final class IPConnect implements InboundPacket, FingerprintInitPacket {
    private final int fingerprintCounter;

    public IPConnect(TransformiceByteBuf in) {
        in.readInt();
        fingerprintCounter = in.readByte();
        in.readUTF();
        in.readUTF();
    }

    @Override
    public int getFingerprintCounter() {
        return fingerprintCounter;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fingerprintCounter", fingerprintCounter)
                .toString();
    }
}
