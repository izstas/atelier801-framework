package com.atelier801.transformice.event;

import com.atelier801.transformice.RoomMouse;

/**
 * This event gets triggered when a mouse (a player) dies.
 */
public class RoomMouseDieEvent extends RoomMouseEvent {
    public RoomMouseDieEvent(RoomMouse mouse) {
        super(mouse);
    }
}
