package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.181
@InboundTribullePacket.Label("ET_SignaleChangementCodeMaisonTFM")
public final class IPTribeHouseMap implements InboundTribullePacket {
    private final String changer;
    private final int houseMap;

    public IPTribeHouseMap(TransformiceByteBuf in) {
        changer = in.readUTF();
        houseMap = in.readInt();
    }

    public String getChanger() {
        return changer;
    }

    public int getHouseMap() {
        return houseMap;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("changer", changer)
                .add("houseMap", houseMap)
                .toString();
    }
}
