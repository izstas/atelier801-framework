package com.atelier801.transformice.client.proto.data;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.263
@Getter @ToString
public final class DTribeMember {
    private final int id;
    private final String name;
    private final int rankId;
    private final int joiningTime;
    private final int lastConnectionTime;
    private final DLocation location;
    private final boolean online;

    public DTribeMember(TransformiceByteBuf in) {
        id = in.readInt();
        in.readInt();
        name = in.readUTF();
        rankId = in.readInt();
        joiningTime = in.readInt();
        lastConnectionTime = in.readInt();
        location = new DLocation(in);
        online = in.readBoolean();
    }
}
