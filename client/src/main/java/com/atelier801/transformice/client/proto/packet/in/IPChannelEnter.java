package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.213
@InboundTribullePacket.Label("ET_SignaleRejointCanal")
@Getter @ToString
public final class IPChannelEnter implements InboundTribullePacket {
    private final int id;
    private final String name;
    private final List<Integer> memberIds;
    private final List<String> memberNames;

    public IPChannelEnter(TransformiceByteBuf in) {
        id = in.readInt();
        name = in.readUTF();
        in.readBoolean();
        memberIds = in.readList(in.readShort(), in::readInt);
        memberNames = in.readList(in.readShort(), in::readUTF);
    }
}
