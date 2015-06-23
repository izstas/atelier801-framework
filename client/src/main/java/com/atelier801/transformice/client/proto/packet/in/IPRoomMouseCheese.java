package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;
import java.util.List;

// Valid for 1.252
@InboundLegacyPacket.Code(major = 5, minor = 19)
@Getter @ToString
public final class IPRoomMouseCheese implements InboundLegacyPacket {
    private final int mouseId;

    public IPRoomMouseCheese(List<String> in) {
        mouseId = Integer.parseInt(in.get(0));
    }
}
