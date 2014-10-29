package com.atelier801.transformice.client.proto.data;

import java.util.List;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;

// Valid for 1.201
public final class DRoomMouse {
    private static final Splitter splitter = Splitter.on('#');

    private final String name;
    private final int id;

    public DRoomMouse(String in) {
        this(splitter.splitToList(in));
    }

    public DRoomMouse(List<String> in) {
        name = in.get(0);
        id = Integer.parseInt(in.get(1));
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("id", id)
                .toString();
    }
}
