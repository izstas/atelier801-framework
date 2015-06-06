package com.atelier801.transformice.client.proto.data;

import lombok.*;

import com.atelier801.transformice.Location;
import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// While 1.242 doesn't have this structure anymore, it's still convenient to have
@Getter @ToString
public final class DLocation {
    private final int game;
    private final String room;

    public DLocation(TransformiceByteBuf in) {
        game = in.readInt();
        room = in.readUTF();
    }

    public Location toLocation() {
        return new Location(Location.Game.valueOf(game), room);
    }
}
