package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import com.google.common.base.MoreObjects;

// Valid for 1.212
@InboundLegacyPacket.Code(major = 8, minor = 5)
public final class IPRoomMouseDie implements InboundLegacyPacket {
    private final int mouseId;
    private final int mouseNewScore;

    public IPRoomMouseDie(List<String> in) {
        mouseId = Integer.parseInt(in.get(0));
        mouseNewScore = Integer.parseInt(in.get(2));
    }

    public int getMouseId() {
        return mouseId;
    }

    public int getMouseNewScore() {
        return mouseNewScore;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mouseId", mouseId)
                .add("mouseNewScore", mouseNewScore)
                .toString();
    }
}
