package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.data.DTribeMember;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleConnexionMembres")
public final class IPTribeMemberConnectBatch implements InboundTribullePacket {
    private final List<DTribeMember> members;

    public IPTribeMemberConnectBatch(TransformiceByteBuf in) {
        members = in.readList(in.readShort(), DTribeMember::new);
    }

    public List<DTribeMember> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("members", members)
                .toString();
    }
}
