package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.247
@InboundTribullePacket.Label("ET_SignaleDeconnexionMembre")
@Getter @ToString
public final class IPTribeMemberDisconnect implements InboundTribullePacket {
    private final int game;
    private final int memberId;

    public IPTribeMemberDisconnect(TransformiceByteBuf in) {
        game = in.readInt();
        memberId = in.readInt();
        in.readUTF();
    }
}
