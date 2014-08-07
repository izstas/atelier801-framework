package com.atelier801.transformice.client.proto.packet.out;

import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundTribullePacket.Label("ST_EnvoitMessageCanal")
public final class OPChannelMessage extends OutboundReferencedTribullePacket {
    private final int channelId;
    private final String message;

    public OPChannelMessage(int channelId, String message) {
        this.channelId = channelId;
        this.message = message;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(referenceId);
        out.writeInt(channelId);
        out.writeUTF(message);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("channelId", channelId)
                .add("message", message)
                .toString();
    }
}
