package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleMessageCanal")
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

    public int getChannelId() {
        return channelId;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public int getSenderCommunity() {
        return senderCommunity;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channelId", channelId)
                .add("sender", sender)
                .add("message", message)
                .add("senderCommunity", senderCommunity)
                .toString();
    }
}
