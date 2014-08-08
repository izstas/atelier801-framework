package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleDeconnexionMembres")
public final class IPTribeMemberDisconnectBatch implements InboundTribullePacket {
    private final int game;
    private final List<Integer> ids;

    public IPTribeMemberDisconnectBatch(TransformiceByteBuf in) {
        game = in.readInt();
        ids = in.readList(in.readShort(), in::readInt);
    }

    public int getGame() {
        return game;
    }

    public List<Integer> getIds() {
        return ids;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("game", game)
                .add("ids", ids)
                .toString();
    }
}
