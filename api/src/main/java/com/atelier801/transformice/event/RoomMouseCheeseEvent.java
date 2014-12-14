package com.atelier801.transformice.event;

import com.atelier801.transformice.RoomMouse;

/**
 * This event gets triggered when a mouse (a player) gets the cheese.
 */
public class RoomMouseCheeseEvent extends RoomMouseEvent {
    public RoomMouseCheeseEvent(RoomMouse mouse) {
        super(mouse);
    }
}
