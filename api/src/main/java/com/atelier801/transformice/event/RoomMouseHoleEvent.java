package com.atelier801.transformice.event;

import java.time.Duration;

import com.atelier801.transformice.RoomMouse;

/**
 * This event gets triggered when a mouse (a player) enters the hole.
 */
public class RoomMouseHoleEvent extends RoomMouseEvent {
    private final int position;
    private final Duration time;

    public RoomMouseHoleEvent(RoomMouse mouse, int position, Duration time) {
        super(mouse);
        this.position = position;
        this.time = time;
    }

    public int getPosition() {
        return position;
    }

    public Duration getTime() {
        return time;
    }
}
