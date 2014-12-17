package com.atelier801.transformice.client.proto.packet.out;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.213
@OutboundTribullePacket.Label("ST_RejoindreCanal")
@AllArgsConstructor @ToString
public final class OPChannelEnter extends OutboundReferencedTribullePacket {
    private final String name;

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(referenceId);
        out.writeUTF(name);
    }
}
