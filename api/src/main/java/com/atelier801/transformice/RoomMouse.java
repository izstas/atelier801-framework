package com.atelier801.transformice;

/**
 * Represents a mouse (a player) in the room.
 */
public interface RoomMouse {
    String getName();
    boolean isDead();
    int getScore();
    boolean hasCheese();
}
