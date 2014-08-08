package com.atelier801.transformice.client.proto.data;

import java.util.List;
import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
public final class DTribeRank {
    private final int id;
    private final String name;
    private final int position;
    private final List<Boolean> permissions;

    public DTribeRank(TransformiceByteBuf in) {
        id = in.readInt();
        name = in.readUTFBytes(20);
        in.readBoolean();
        in.readInt();
        position = in.readByte();
        permissions = in.readList(in.readShort(), in::readBoolean);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public List<Boolean> getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("position", position)
                .add("permissions", permissions)
                .toString();
    }
}
