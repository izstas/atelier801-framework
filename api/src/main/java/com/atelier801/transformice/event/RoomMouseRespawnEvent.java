package com.atelier801.transformice.event;

import com.atelier801.transformice.RoomMouse;

/**
 * This event gets triggered when a mouse (player) respawns.
 */
public class RoomMouseRespawnEvent extends RoomMouseEvent {
    public RoomMouseRespawnEvent(RoomMouse mouse) {
        super(mouse);
    }
}
