package com.atelier801.transformice.event;

import com.atelier801.transformice.Chat;
import com.atelier801.transformice.Community;

/**
 * An interface for {@link com.atelier801.transformice.Chat} message events.
 */
public interface ChatMessageEvent {
    Chat getChat();

    String getSenderName();
    Community getSenderCommunity();
    String getMessage();
}
