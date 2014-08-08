package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.data.DTribeMember;
import com.atelier801.transformice.client.proto.packet.out.OPTribeMembersRequest;

// Valid for 1.180
@InboundTribullePacket.Label("ET_ResultatMembresTribu")
public final class IPTribeMembers extends InboundReferencingTribullePacket<OPTribeMembersRequest> {
    private final List<DTribeMember> members;

    public IPTribeMembers(TransformiceByteBuf in) {
        referenceId = in.readInt();
        members = in.readList(in.readShort(), DTribeMember::new);
    }

    public List<DTribeMember> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("reference", reference)
                .add("members", members)
                .toString();
    }
}
