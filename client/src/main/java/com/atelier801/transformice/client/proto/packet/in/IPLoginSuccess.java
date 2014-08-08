package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import com.google.common.base.MoreObjects;

// Valid for 1.180
@InboundLegacyPacket.Code(major = 26, minor = 8)
public final class IPLoginSuccess implements InboundLegacyPacket {
    private final String mouseName;
    private final int mouseId;

    public IPLoginSuccess(List<String> in) {
        mouseName = in.get(0);
        mouseId = Integer.parseInt(in.get(1));
    }

    public String getMouseName() {
        return mouseName;
    }

    public int getMouseId() {
        return mouseId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mouseName", mouseName)
                .add("mouseId", mouseId)
                .toString();
    }
}
