package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundPacket.Code(major = 28, minor = 17)
public final class OPClient implements OutboundPacket {
    @Override
    public void write(TransformiceByteBuf out) {
        out.writeUTF("en"); // Capabilities.language
        out.writeUTF("Windows 8.1"); // Capabilities.os
        out.writeUTF("WIN 14,0,0,145"); // Capabilities.version
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).toString();
    }
}
