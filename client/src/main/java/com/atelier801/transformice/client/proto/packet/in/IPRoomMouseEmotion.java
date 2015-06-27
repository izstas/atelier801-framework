package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;
import java.util.Optional;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.252
@InboundPacket.Code(major = 8, minor = 1)
@Getter @ToString
public final class IPRoomMouseEmotion implements InboundPacket {
    private final int mouseId;
    private final int emotion;
    private final Optional<String> emotionArg;

    public IPRoomMouseEmotion(TransformiceByteBuf in) {
        mouseId = in.readInt();
        emotion = in.readByte();
        // TFM explicitly checks if the action equals 10, I'm trying a different approach
        emotionArg = in.isReadable() ? Optional.of(in.readUTF()) : Optional.empty();
    }
}
