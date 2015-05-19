package com.atelier801.transformice.client.proto.data;

import java.util.List;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.242 - needs refactoring!
public final class DTribeMember {
    private final int id;
    private final String name;
    private final int rankId;
    private final int joiningTime;
    private final int lastConnectionTime;
    private final List<DLocation> locations;

    public DTribeMember(TransformiceByteBuf in) {
        id = in.readInt();
        in.readInt();
        name = in.readUTFBytes(20);
        rankId = in.readInt();
        joiningTime = in.readInt();
        lastConnectionTime = in.readInt();

        DLocation location = new DLocation(in);
        locations = in.readBoolean() ? ImmutableList.of(location) : ImmutableList.of();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRankId() {
        return rankId;
    }

    public int getJoiningTime() {
        return joiningTime;
    }

    public int getLastConnectionTime() {
        return lastConnectionTime;
    }

    public List<DLocation> getLocations() {
        return locations;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("rankId", rankId)
                .add("joiningTime", joiningTime)
                .add("lastConnectionTime", lastConnectionTime)
                .add("locations", locations)
                .toString();
    }
}
