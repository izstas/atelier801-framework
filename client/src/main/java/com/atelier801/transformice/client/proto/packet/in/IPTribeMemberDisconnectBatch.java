package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;
import java.util.List;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.247
@InboundTribullePacket.Label("ET_SignaleDeconnexionMembres")
@Getter @ToString
public final class IPTribeMemberDisconnectBatch implements InboundTribullePacket {
    private final int game;
    private final List<Integer> memberIds;

    public IPTribeMemberDisconnectBatch(TransformiceByteBuf in) {
        game = in.readInt();
        memberIds = in.readList(in.readShort(), in::readInt);
    }
}
