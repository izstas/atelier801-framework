package com.atelier801.transformice.client.proto.packet.out;

import java.util.List;
import com.google.common.base.MoreObjects;

// Valid for 1.190
@OutboundLegacyPacket.Code(major = 26, minor = 2)
public final class OPKeepAlive implements OutboundLegacyPacket {
    @Override
    public void write(List<String> out) {
        // Empty
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }
}
