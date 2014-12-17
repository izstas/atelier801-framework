package com.atelier801.transformice.event;

import com.atelier801.transformice.ChatChannel;
import com.atelier801.transformice.Community;

/**
 * This event gets triggered when a new message appears in the chat channel you've entered.
 */
public class ChannelMessageEvent extends ChatMessageEvent {
    public ChannelMessageEvent(ChatChannel chat, String senderName, Community senderCommunity, String message) {
        super(chat, senderName, senderCommunity, message);
    }

    @Override
    public ChatChannel getChat() {
        return (ChatChannel) super.getChat();
    }
}
