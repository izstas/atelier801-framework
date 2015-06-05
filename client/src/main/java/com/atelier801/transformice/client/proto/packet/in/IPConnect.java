package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.fingerprint.FingerprintInitPacket;

// Valid for 1.247
@InboundPacket.Code(major = 26, minor = 3)
@Getter @ToString
public final class IPConnect implements InboundPacket, FingerprintInitPacket {
    private final int fingerprintCounter;
    private final int loginCode;

    public IPConnect(TransformiceByteBuf in) {
        in.readInt();
        fingerprintCounter = in.readByte();
        in.readUTF();
        in.readUTF();
        loginCode = in.readInt();
    }
}
