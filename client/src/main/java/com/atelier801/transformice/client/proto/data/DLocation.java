package com.atelier801.transformice.client.proto.data;

import com.google.common.base.Objects;

import com.atelier801.transformice.Location;
import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
public final class DLocation {
    private final int game;
    private final String room;

    public DLocation(TransformiceByteBuf in) {
        game = in.readInt();
        room = in.readUTF();
    }

    public int getGame() {
        return game;
    }

    public String getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("game", game)
                .add("room", room)
                .toString();
    }

    public Location toLocation() {
        return new Location(Location.Game.valueOf(game), room);
    }
}
