package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.213
@InboundTribullePacket.Label("ET_SignaleMembresRejoignentCanal")
@Getter @ToString
public final class IPChannelMemberJoinBatch implements InboundTribullePacket {
    private final int channelId;
    private final List<Integer> ids;
    private final List<String> names;

    public IPChannelMemberJoinBatch(TransformiceByteBuf in) {
        channelId = in.readInt();
        ids = in.readList(in.readShort(), in::readInt);
        names = in.readList(in.readShort(), in::readUTF);
    }
}
