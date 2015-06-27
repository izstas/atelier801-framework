package com.atelier801.transformice.client.proto.packet.out;

import lombok.*;
import java.util.Optional;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.252
@OutboundPacket.Code(major = 8, minor = 1)
@AllArgsConstructor @ToString
public final class OPRoomMouseEmotion implements OutboundPacket {
    private final int emotion;
    private final Optional<String> emotionArg;

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeByte(emotion);
        emotionArg.ifPresent(out::writeUTF);
    }
}
