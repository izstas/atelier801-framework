package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.252
@InboundPacket.Code(major = 8, minor = 5)
@Getter @ToString
public final class IPRoomMouseEmoticon implements InboundPacket {
    private final int mouseId;
    private final int emoticon;

    public IPRoomMouseEmoticon(TransformiceByteBuf in) {
        mouseId = in.readInt();
        emoticon = in.readByte();
    }
}
