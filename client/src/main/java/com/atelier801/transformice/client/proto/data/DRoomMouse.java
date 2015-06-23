package com.atelier801.transformice.client.proto.data;

import lombok.*;
import java.util.List;
import com.google.common.base.Splitter;

// Valid for 1.252
@Getter @ToString
public final class DRoomMouse {
    private static final Splitter splitter = Splitter.on('#');

    private final String name;
    private final int id;
    private final boolean shaman;
    private final boolean dead;
    private final int score;
    @Getter(AccessLevel.NONE) // We want the getter for the field below to be called hasCheese, not isCheese
    private final boolean cheese;
    private final DTitle title;
    private final DOutfit outfit;
    private final int furColor;
    private final int shamanColor;
    private final int nameColor;

    public DRoomMouse(String in) {
        this(splitter.splitToList(in));
    }

    public DRoomMouse(List<String> in) {
        name = in.get(0);
        id = Integer.parseInt(in.get(1));
        shaman = in.get(2).equals("1");
        // TFM also checks if the field below is less than 2 and appears not to add id -> mouse mapping if it's not
        dead = Integer.parseInt(in.get(3)) > 0;
        score = Integer.parseInt(in.get(4));
        cheese = in.get(5).equals("1");
        title = new DTitle(in.get(6));
        outfit = new DOutfit(in.get(8));
        furColor = Integer.parseInt(in.get(10), 16);
        shamanColor = Integer.parseInt(in.get(11), 16);
        nameColor = Integer.parseInt(in.get(12), 16);
    }

    public boolean hasCheese() {
        return cheese;
    }
}
