package com.atelier801.transformice.event;

import lombok.*;

import com.atelier801.transformice.ChatChannel;

/**
 * This event gets triggered when you enter a new chat channel.
 */
@AllArgsConstructor @Getter
public class ChannelEnterEvent extends Event {
    private final ChatChannel channel;
}
