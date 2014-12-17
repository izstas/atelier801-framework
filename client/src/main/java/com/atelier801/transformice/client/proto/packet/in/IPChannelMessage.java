package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.213
@InboundTribullePacket.Label("ET_SignaleMessageCanal")
@Getter @ToString
public final class IPChannelMessage implements InboundTribullePacket {
    private final int channelId;
    private final String sender;
    private final String message;
    private final int senderCommunity;

    public IPChannelMessage(TransformiceByteBuf in) {
        channelId = in.readInt();
        sender = in.readUTF();
        message = in.readUTF();
        senderCommunity = in.readByte();
    }
}
