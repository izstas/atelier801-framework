package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.data.DTribeRank;

// Valid for 1.180
@InboundTribullePacket.Label("ET_ResultatInformationsTribu")
public final class IPTribe implements InboundTribullePacket {
    private final int id;
    private final String name;
    private final String greeting;
    private final int houseMap;
    private final List<DTribeRank> ranks;

    public IPTribe(TransformiceByteBuf in) {
        in.readInt();
        id = in.readInt();
        name = in.readUTFBytes(50);
        greeting = in.readUTF();
        houseMap = in.readInt();
        in.readInt();
        ranks = in.readList(in.readShort(), DTribeRank::new);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGreeting() {
        return greeting;
    }

    public int getHouseMap() {
        return houseMap;
    }

    public List<DTribeRank> getRanks() {
        return ranks;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("greeting", greeting)
                .add("houseMap", houseMap)
                .add("ranks", ranks)
                .toString();
    }
}
