package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleDeconnexionMembre")
public final class IPTribeMemberDisconnect implements InboundTribullePacket {
    private final int game;
    private final int id;

    public IPTribeMemberDisconnect(TransformiceByteBuf in) {
        game = in.readInt();
        id = in.readInt();
        in.readUTF();
    }

    public int getGame() {
        return game;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("game", game)
                .add("id", id)
                .toString();
    }
}
