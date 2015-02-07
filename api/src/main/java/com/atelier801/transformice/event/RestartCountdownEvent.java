package com.atelier801.transformice.event;

import lombok.*;

/**
 * This event gets triggered when server notifies of impending restart ("The server will restart in...")
 */
@AllArgsConstructor @Getter
public final class RestartCountdownEvent extends Event {
    private final int countdown;
}
