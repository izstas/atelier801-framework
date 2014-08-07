package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.data.DLocation;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleModificationLocalisationMembreTribu")
public final class IPTribeMemberLocation implements InboundTribullePacket {
    private final int id;
    private final DLocation location;

    public IPTribeMemberLocation(TransformiceByteBuf in) {
        id = in.readInt();
        location = new DLocation(in);
    }

    public int getId() {
        return id;
    }

    public DLocation getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("location", location)
                .toString();
    }
}
