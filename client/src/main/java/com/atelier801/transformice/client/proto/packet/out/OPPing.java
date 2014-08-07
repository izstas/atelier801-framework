package com.atelier801.transformice.client.proto.packet.out;

import java.util.List;
import com.google.common.base.Objects;

// Valid for 1.180
@OutboundLegacyPacket.Code(major = 26, minor = 2)
public final class OPPing implements OutboundLegacyPacket {
    @Override
    public void write(List<String> out) {
        // Empty
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).toString();
    }
}