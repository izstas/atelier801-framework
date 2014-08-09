package com.atelier801.transformice.event;

import com.atelier801.transformice.Chat;
import com.atelier801.transformice.Community;

/**
 * This event gets triggered by a private message.
 */
public class PrivateMessageEvent extends ChatMessageEvent {
    public PrivateMessageEvent(Chat chat, String senderName, Community senderCommunity, String message) {
        super(chat, senderName, senderCommunity, message);
    }
}
