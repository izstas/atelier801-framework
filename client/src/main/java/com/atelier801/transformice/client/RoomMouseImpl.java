package com.atelier801.transformice.client;

import com.atelier801.transformice.RoomMouse;
import com.atelier801.transformice.client.proto.data.DRoomMouse;

final class RoomMouseImpl implements RoomMouse, Pooled<DRoomMouse> {
    final TransformiceClient transformice;
    final int id;
    private String name;

    RoomMouseImpl(TransformiceClient transformice, int id) {
        this.transformice = transformice;
        this.id = id;
    }

    @Override
    public void update(DRoomMouse data) {
        name = data.getName();
    }

    @Override
    public String getName() {
        return name;
    }
}
