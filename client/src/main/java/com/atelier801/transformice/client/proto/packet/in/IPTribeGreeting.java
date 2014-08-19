package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.181
@InboundTribullePacket.Label("ET_SignaleChangementMessageJour")
public final class IPTribeGreeting implements InboundTribullePacket {
    private final String changer;
    private final String greeting;

    public IPTribeGreeting(TransformiceByteBuf in) {
        changer = in.readUTF();
        greeting = in.readUTF();
    }

    public String getChanger() {
        return changer;
    }

    public String getGreeting() {
        return greeting;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("changer", changer)
                .add("greeting", greeting)
                .toString();
    }
}
