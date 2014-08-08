package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreMoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.data.DTribeMember;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleNouveauMembre")
public final class IPTribeMemberJoin implements InboundTribullePacket {
    private final DTribeMember member;

    public IPTribeMemberJoin(TransformiceByteBuf in) {
        member = new DTribeMember(in);
    }

    public DTribeMember getMember() {
        return member;
    }

    @Override
    public String toString() {
        return MoreMoreObjects.toStringHelper(this)
                .add("member", member)
                .toString();
    }
}
