package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.213
@InboundTribullePacket.Label("ET_SignaleRejointCanal")
@Getter @ToString
public final class IPChannelEnter implements InboundTribullePacket {
    private final int id;
    private final String name;

    public IPChannelEnter(TransformiceByteBuf in) {
        id = in.readInt();
        name = in.readUTF();
        in.readBoolean(); // Flag indicating if this is a custom chat?
        in.readList(in.readShort(), in::readInt); // Member ids
        in.readList(in.readShort(), in::readUTF); // Member names
    }
}
