package com.atelier801.transformice;

import rx.Observable;

import com.atelier801.transformice.event.ChannelQuitEvent;

/**
 * Represents a chat channel which can be entered in-game by using the /chat command.
 */
public interface ChatChannel extends Chat {
    String getName();

    Observable<ChannelQuitEvent> quit();
}
