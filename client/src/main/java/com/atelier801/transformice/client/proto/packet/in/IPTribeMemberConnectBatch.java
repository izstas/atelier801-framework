package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;
import java.util.List;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.data.DTribeMember;

// Valid for 1.247
@InboundTribullePacket.Label("ET_SignaleConnexionMembres")
@Getter @ToString
public final class IPTribeMemberConnectBatch implements InboundTribullePacket {
    private final List<DTribeMember> members;

    public IPTribeMemberConnectBatch(TransformiceByteBuf in) {
        members = in.readList(in.readShort(), DTribeMember::new);
    }
}
