package com.atelier801.transformice.event;

import com.atelier801.transformice.RoomMouse;

/**
 * This event gets triggered when a new mouse (player) joins the room.
 */
public class RoomMouseJoinEvent extends RoomMouseEvent {
    public RoomMouseJoinEvent(RoomMouse mouse) {
        super(mouse);
    }
}
