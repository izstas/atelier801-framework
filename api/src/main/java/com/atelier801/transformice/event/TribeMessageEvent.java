package com.atelier801.transformice.event;

import com.atelier801.transformice.Chat;
import com.atelier801.transformice.Community;
import com.atelier801.transformice.TribeMember;

/**
 * This event gets triggered by a tribe chat message.
 */
public class TribeMessageEvent extends ChatMessageEvent {
    private final TribeMember sender;

    public TribeMessageEvent(Chat chat, TribeMember sender, String senderName, Community senderCommunity, String message) {
        super(chat, senderName, senderCommunity, message);
        this.sender = sender;
    }

    public TribeMember getSender() {
        return sender;
    }
}
