package com.atelier801.transformice.client.proto.packet.out;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundPacket.Code(major = 1, minor = 1)
public final class OPWrapperLegacy implements OutboundPacket {
    private final String payload;

    public OPWrapperLegacy(String payload) {
        this.payload = payload;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeUTF(payload);
    }
}
