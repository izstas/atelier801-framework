package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundPacket.Code(major = 6, minor = 6)
public final class IPRoomMessage implements InboundPacket {
    private final int senderMouseId;
    private final String sender;
    private final int senderCommunity;
    private final String message;

    public IPRoomMessage(TransformiceByteBuf in) {
        senderMouseId = in.readInt();
        sender = in.readUTF();
        senderCommunity = in.readByte();
        message = in.readUTF();
    }

    public int getSenderMouseId() {
        return senderMouseId;
    }

    public String getSender() {
        return sender;
    }

    public int getSenderCommunity() {
        return senderCommunity;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("senderMouseId", senderMouseId)
                .add("sender", sender)
                .add("senderCommunity", senderCommunity)
                .add("message", message)
                .toString();
    }
}
