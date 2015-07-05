package com.atelier801.transformice.client.proto.packet.out;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.255
@OutboundTribullePacket.Label("ST_RequeteDemandeInfosJeuUtilisateur")
@AllArgsConstructor @ToString
public final class OPTribulleRequest extends OutboundReferencedTribullePacket {
    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(referenceId);
    }
}
