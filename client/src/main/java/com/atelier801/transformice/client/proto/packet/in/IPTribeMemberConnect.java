package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.data.DTribeMember;

// Valid for 1.247
@InboundTribullePacket.Label("ET_SignaleConnexionMembre")
@Getter @ToString
public final class IPTribeMemberConnect implements InboundTribullePacket {
    private final DTribeMember member;

    public IPTribeMemberConnect(TransformiceByteBuf in) {
        member = new DTribeMember(in);
    }
}
