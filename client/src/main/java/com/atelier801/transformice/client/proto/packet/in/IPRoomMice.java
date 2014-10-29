package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

import com.atelier801.transformice.client.proto.data.DRoomMouse;

@InboundLegacyPacket.Code(major = 8, minor = 9)
public final class IPRoomMice implements InboundLegacyPacket {
    private final List<DRoomMouse> mice;

    public IPRoomMice(List<String> in) {
        ImmutableList.Builder<DRoomMouse> miceBuilder = ImmutableList.builder();
        in.forEach(mouseIn -> miceBuilder.add(new DRoomMouse(mouseIn)));
        mice = miceBuilder.build();
    }

    public List<DRoomMouse> getMice() {
        return mice;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mice", mice)
                .toString();
    }
}
