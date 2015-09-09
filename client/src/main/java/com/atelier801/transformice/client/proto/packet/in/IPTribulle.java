package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.packet.out.OPTribulleRequest;

// Valid for 1.263
@InboundTribullePacket.Label("ET_ReponseDemandeInfosJeuUtilisateur")
@Getter @ToString
public final class IPTribulle extends InboundReferencingTribullePacket<OPTribulleRequest> {
    private final int id;

    public IPTribulle(TransformiceByteBuf in) {
        referenceId = in.readInt();
        id = in.readInt();
        in.readInt();
        in.readInt();
        in.readInt();
        in.readUTF();
    }
}
