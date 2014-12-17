package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.213
@InboundTribullePacket.Label("ET_SignaleMembresQuittentCanal")
@Getter @ToString
public final class IPChannelMemberLeaveBatch implements InboundTribullePacket {
    private final int channelId;
    private final List<Integer> memberIds;

    public IPChannelMemberLeaveBatch(TransformiceByteBuf in) {
        channelId = in.readInt();
        memberIds = in.readList(in.readShort(), in::readInt);
    }
}
