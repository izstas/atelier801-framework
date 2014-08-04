package com.atelier801.transformice.event;

import com.atelier801.transformice.Chat;
import com.atelier801.transformice.Community;

/**
 * This event gets triggered on a tribe chat message.
 */
public class TribeMessageEvent extends Event implements ChatMessageEvent {
    private final Chat chat;
    private final String senderName;
    private final Community senderCommunity;
    private final String message;

    public TribeMessageEvent(Chat chat, String senderName, Community senderCommunity, String message) {
        this.chat = chat;
        this.senderName = senderName;
        this.senderCommunity = senderCommunity;
        this.message = message;
    }

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public String getSenderName() {
        return senderName;
    }

    @Override
    public Community getSenderCommunity() {
        return senderCommunity;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
