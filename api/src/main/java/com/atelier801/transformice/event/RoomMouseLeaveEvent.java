package com.atelier801.transformice.event;

import com.atelier801.transformice.RoomMouse;

/**
 * This event gets triggered when a mouse (player) leaves the room.
 */
public class RoomMouseLeaveEvent extends RoomMouseEvent {
    public RoomMouseLeaveEvent(RoomMouse mouse) {
        super(mouse);
    }
}
