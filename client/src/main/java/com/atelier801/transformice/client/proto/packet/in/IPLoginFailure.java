package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import com.google.common.base.Objects;

// Valid for 1.180
@InboundLegacyPacket.Code(major = 26, minor = 3)
public final class IPLoginFailure implements InboundLegacyPacket {
    private final int reason;

    public IPLoginFailure(List<String> in) {
        reason = in.size();
    }

    public int getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("reason", reason)
                .toString();
    }
}
