package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.213
@InboundTribullePacket.Label("ET_SignaleQuitteCanal")
@Getter @ToString
public final class IPChannelQuit implements InboundTribullePacket {
    private final int channelId;

    public IPChannelQuit(TransformiceByteBuf in) {
        channelId = in.readInt();
    }
}
