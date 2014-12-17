package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.213
@InboundTribullePacket.Label("ET_SignaleMembreRejointCanal")
@Getter @ToString
public final class IPChannelMemberJoin implements InboundTribullePacket {
    private final int channelId;
    private final int id;
    private final String name;

    public IPChannelMemberJoin(TransformiceByteBuf in) {
        channelId = in.readInt();
        id = in.readInt();
        name = in.readUTF();
    }
}
