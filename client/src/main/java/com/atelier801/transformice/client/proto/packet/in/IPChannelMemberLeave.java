package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.213
@InboundTribullePacket.Label("ET_SignaleMembreQuitteCanal")
@Getter @ToString
public final class IPChannelMemberLeave implements InboundTribullePacket {
    private final int channelId;
    private final int memberId;

    public IPChannelMemberLeave(TransformiceByteBuf in) {
        channelId = in.readInt();
        memberId = in.readInt();
    }
}
