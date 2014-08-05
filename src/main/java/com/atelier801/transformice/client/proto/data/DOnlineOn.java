package com.atelier801.transformice.client.proto.data;

import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
public final class DOnlineOn {
    private final int service;
    private final String location;

    public DOnlineOn(TransformiceByteBuf in) {
        service = in.readInt();
        location = in.readUTF();
    }

    public int getService() {
        return service;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("service", service)
                .add("location", location)
                .toString();
    }
}
