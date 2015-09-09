package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;
import java.util.List;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.data.DTribeRank;

// Valid for 1.263
@InboundTribullePacket.Label("ET_ResultatInformationsTribu")
@Getter @ToString
public final class IPTribe implements InboundTribullePacket {
    private final int id;
    private final String name;
    private final String greeting;
    private final int houseMap;
    private final List<DTribeRank> ranks;

    public IPTribe(TransformiceByteBuf in) {
        in.readInt();
        id = in.readInt();
        name = in.readUTF();
        greeting = in.readUTF();
        houseMap = in.readInt();
        in.readInt();
        ranks = in.readList(in.readShort(), DTribeRank::new);
    }
}
