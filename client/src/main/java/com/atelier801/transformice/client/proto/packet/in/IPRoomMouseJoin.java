package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;
import java.util.List;

import com.atelier801.transformice.client.proto.data.DRoomMouse;

// Valid for 1.252
@InboundLegacyPacket.Code(major = 8, minor = 8)
@Getter @ToString
public final class IPRoomMouseJoin implements InboundLegacyPacket {
    private final DRoomMouse mouse;

    public IPRoomMouseJoin(List<String> in) {
        mouse = new DRoomMouse(in.get(0));
    }
}
