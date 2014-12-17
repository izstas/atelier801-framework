package com.atelier801.transformice.client.proto.packet.out;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.213
@OutboundTribullePacket.Label("ST_QuitterCanal")
@AllArgsConstructor @ToString
public final class OPChannelQuit extends OutboundReferencedTribullePacket {
    private final int channelId;

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(referenceId);
        out.writeInt(channelId);
    }
}
