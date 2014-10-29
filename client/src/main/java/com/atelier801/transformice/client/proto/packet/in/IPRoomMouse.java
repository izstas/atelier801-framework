package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.data.DRoomMouse;

// Valid for 1.201
@InboundLegacyPacket.Code(major = 8, minor = 8)
public final class IPRoomMouse implements InboundLegacyPacket {
    private final DRoomMouse mouse;

    public IPRoomMouse(List<String> in) {
        mouse = new DRoomMouse(in.get(0));
    }

    public DRoomMouse getMouse() {
        return mouse;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mouse", mouse)
                .toString();
    }
}
