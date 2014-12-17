package com.atelier801.transformice.event;

import lombok.*;

import com.atelier801.transformice.ChatChannel;

/**
 * This event gets triggered when you quit a chat channel.
 */
@AllArgsConstructor @Getter
public class ChannelQuitEvent extends Event {
    private final ChatChannel channel;
}
