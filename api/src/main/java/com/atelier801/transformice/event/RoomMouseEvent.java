package com.atelier801.transformice.event;

import com.atelier801.transformice.RoomMouse;

/**
 * A base class for events related to some mouse (player) in the room.
 */
public abstract class RoomMouseEvent extends Event {
    private final RoomMouse mouse;

    public RoomMouseEvent(RoomMouse mouse) {
        this.mouse = mouse;
    }

    public RoomMouse getMouse() {
        return mouse;
    }
}
