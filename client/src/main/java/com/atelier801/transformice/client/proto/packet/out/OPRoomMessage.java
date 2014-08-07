package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundPacket.Code(major = 6, minor = 8)
public final class OPRoomMessage implements OutboundPacket {
    private final String message;

    public OPRoomMessage(String message) {
        this.message = message;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeUTF(message);
        out.writeByte(32);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("message", message)
                .toString();
    }
}
