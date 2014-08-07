package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.data.DTribeMember;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleConnexionMembre")
public final class IPTribeMemberConnect implements InboundTribullePacket {
    private final DTribeMember member;

    public IPTribeMemberConnect(TransformiceByteBuf in) {
        member = new DTribeMember(in);
    }

    public DTribeMember getMember() {
        return member;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("member", member)
                .toString();
    }
}
