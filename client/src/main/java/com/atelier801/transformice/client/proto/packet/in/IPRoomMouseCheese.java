package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import com.google.common.base.MoreObjects;

// Valid for 1.212
@InboundLegacyPacket.Code(major = 5, minor = 19)
public final class IPRoomMouseCheese implements InboundLegacyPacket {
    private final int mouseId;

    public IPRoomMouseCheese(List<String> in) {
        mouseId = Integer.parseInt(in.get(0));
    }

    public int getMouseId() {
        return mouseId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mouseId", mouseId)
                .toString();
    }
}
