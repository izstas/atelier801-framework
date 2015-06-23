package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;
import java.util.List;

// Valid for 1.252
@InboundLegacyPacket.Code(major = 8, minor = 5)
@Getter @ToString
public final class IPRoomMouseDie implements InboundLegacyPacket {
    private final int mouseId;
    private final int mouseNewScore;
    private final int type;

    public IPRoomMouseDie(List<String> in) {
        mouseId = Integer.parseInt(in.get(0));
        mouseNewScore = Integer.parseInt(in.get(2));
        type = Integer.parseInt(in.get(3));
    }
}
