package com.atelier801.transformice.client.proto.packet.out;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.213
@OutboundTribullePacket.Label("ST_EnvoitMessageCanal")
@AllArgsConstructor @ToString
public final class OPChannelMessage extends OutboundReferencedTribullePacket {
    private final int channelId;
    private final String message;

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(referenceId);
        out.writeInt(channelId);
        out.writeUTF(message);
    }
}
