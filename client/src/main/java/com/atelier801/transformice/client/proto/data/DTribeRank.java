package com.atelier801.transformice.client.proto.data;

import lombok.*;
import java.util.List;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.263
@Getter @ToString
public final class DTribeRank {
    private final int id;
    private final String name;
    private final int position;
    private final List<Boolean> permissions;

    public DTribeRank(TransformiceByteBuf in) {
        id = in.readInt();
        name = in.readUTF();
        in.readBoolean();
        in.readInt();
        position = in.readByte();
        permissions = in.readList(in.readShort(), in::readBoolean);
    }
}
