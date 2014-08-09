package com.atelier801.transformice.event;

import com.atelier801.transformice.Chat;
import com.atelier801.transformice.Community;

/**
 * A base class for {@link com.atelier801.transformice.Chat} message events.
 */
public abstract class ChatMessageEvent extends Event {
    private final Chat chat;
    private final String senderName;
    private final Community senderCommunity;
    private final String message;

    public ChatMessageEvent(Chat chat, String senderName, Community senderCommunity, String message) {
        this.chat = chat;
        this.senderName = senderName;
        this.senderCommunity = senderCommunity;
        this.message = message;
    }

    public Chat getChat() {
        return chat;
    }

    public String getSenderName() {
        return senderName;
    }

    public Community getSenderCommunity() {
        return senderCommunity;
    }

    public String getMessage() {
        return message;
    }
}
