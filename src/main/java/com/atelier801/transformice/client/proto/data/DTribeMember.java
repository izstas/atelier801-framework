package com.atelier801.transformice.client.proto.data;

import java.util.List;
import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
public final class DTribeMember {
    private final int id;
    private final String name;
    private final int rankId;
    private final int joinTime;
    private final int lastOnlineTime;
    private final List<DLocation> onlineLocations;

    public DTribeMember(TransformiceByteBuf in) {
        id = in.readInt();
        in.readInt();
        name = in.readUTFBytes(20);
        rankId = in.readInt();
        joinTime = in.readInt();
        lastOnlineTime = in.readInt();
        onlineLocations = in.readList(in.readShort(), DLocation::new);
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

    public int getJoinTime() {
        return joinTime;
    }

    public int getLastOnlineTime() {
        return lastOnlineTime;
    }

    public List<DLocation> getOnlineLocations() {
        return onlineLocations;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("rankId", rankId)
                .add("joinTime", joinTime)
                .add("lastOnlineTime", lastOnlineTime)
                .add("onlineLocations", onlineLocations)
                .toString();
    }
}
