package com.atelier801.transformice.client.proto.packet.in;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundPacket.Code(major = 1, minor = 1)
public final class IPWrapperLegacy implements InboundPacket {
    private final String payload;

    public IPWrapperLegacy(TransformiceByteBuf in) {
        payload = in.readUTF();
    }

    public String getPayload() {
        return payload;
    }
}
