package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;
import java.util.List;
import com.google.common.collect.ImmutableList;

import com.atelier801.transformice.client.proto.data.DRoomMouse;

// Valid for 1.252
@InboundLegacyPacket.Code(major = 8, minor = 9)
@Getter @ToString
public final class IPRoomMice implements InboundLegacyPacket {
    private final List<DRoomMouse> mice;

    public IPRoomMice(List<String> in) {
        mice = ImmutableList.copyOf(in.stream().map(DRoomMouse::new).iterator());
    }
}
