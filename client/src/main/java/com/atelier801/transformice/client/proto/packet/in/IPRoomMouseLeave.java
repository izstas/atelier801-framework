package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;
import java.util.List;

// Valid for 1.252
@InboundLegacyPacket.Code(major = 8, minor = 7)
@Getter @ToString
public final class IPRoomMouseLeave implements InboundLegacyPacket {
    private final int mouseId;

    public IPRoomMouseLeave(List<String> in) {
        mouseId = Integer.parseInt(in.get(0));
    }
}
