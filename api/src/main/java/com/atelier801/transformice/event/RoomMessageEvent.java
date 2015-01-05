package com.atelier801.transformice.event;

import lombok.*;
import com.atelier801.transformice.Chat;
import com.atelier801.transformice.Community;
import com.atelier801.transformice.RoomMouse;

/**
 * This event gets triggered when a new message appears in the room chat.
 */
@Getter
public class RoomMessageEvent extends ChatMessageEvent {
    private final RoomMouse sender;

    public RoomMessageEvent(Chat chat, RoomMouse sender, String senderName, Community senderCommunity, String message) {
        super(chat, senderName, senderCommunity, message);
        this.sender = sender;
    }
}
