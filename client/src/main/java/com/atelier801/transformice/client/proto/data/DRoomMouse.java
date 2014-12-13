package com.atelier801.transformice.client.proto.data;

import java.util.List;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;

// Valid for 1.212
public final class DRoomMouse {
    private static final Splitter splitter = Splitter.on('#');

    private final String name;
    private final int id;
    private final boolean dead;
    private final int score;
    private final boolean cheese;

    public DRoomMouse(String in) {
        this(splitter.splitToList(in));
    }

    public DRoomMouse(List<String> in) {
        name = in.get(0);
        id = Integer.parseInt(in.get(1));
        // TFM also checks if the field below is <= 2 and doesn't add id -> mouse mapping if it's not?
        dead = Integer.parseInt(in.get(3)) > 0;
        score = Integer.parseInt(in.get(4));
        cheese = in.get(5).equals("1");
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isDead() {
        return dead;
    }

    public int getScore() {
        return score;
    }

    public boolean hasCheese() {
        return cheese;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("id", id)
                .add("dead", dead)
                .add("score", score)
                .add("cheese", cheese)
                .toString();
    }
}
