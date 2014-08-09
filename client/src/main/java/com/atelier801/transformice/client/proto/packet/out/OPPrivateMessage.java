package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundTribullePacket.Label("ST_EnvoitMessagePrive")
public final class OPPrivateMessage extends OutboundReferencedTribullePacket {
    private final String recipient;
    private final String message;

    public OPPrivateMessage(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(referenceId);
        out.writeUTF(recipient);
        out.writeUTF(message);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("recipient", recipient)
                .add("message", message)
                .toString();
    }
}
