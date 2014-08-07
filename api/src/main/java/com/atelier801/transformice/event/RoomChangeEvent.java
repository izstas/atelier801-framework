package com.atelier801.transformice.event;

/**
 * This event gets triggered on a room change.
 */
public class RoomChangeEvent extends Event {
    private final String room;

    public RoomChangeEvent(String room) {
        this.room = room;
    }

    public String getRoom() {
        return room;
    }
}
