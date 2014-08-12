package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundTribullePacket.Label("ET_RecoitMessagePrive")
public final class IPPrivateMessage implements InboundTribullePacket {
    private final String sender;
    private final String message;
    private final int senderCommunity;
    private final boolean outgoing;

    public IPPrivateMessage(TransformiceByteBuf in) {
        sender = in.readUTF();
        message = in.readUTF();
        senderCommunity = in.readByte();
        outgoing = in.readBoolean();
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

    public boolean isOutgoing() {
        return outgoing;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sender", sender)
                .add("message", message)
                .add("senderCommunity", senderCommunity)
                .add("outgoing", outgoing)
                .toString();
    }
}
