package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.263
@InboundTribullePacket.Label("ET_SignaleChangementRang")
@Getter @ToString
public final class IPTribeMemberRank implements InboundTribullePacket {
    private final int id;
    private final int rankId;

    public IPTribeMemberRank(TransformiceByteBuf in) {
        id = in.readInt();
        in.readUTF();
        rankId = in.readInt();
        in.readUTF();
    }
}
