package com.atelier801.transformice.event;

import com.atelier801.transformice.Location;
import com.atelier801.transformice.TribeMember;

/**
 * This event gets triggered when a tribe member disconnects from a game.
 */
public class TribeMemberDisconnectEvent extends TribeMemberEvent {
    private final Location.Game game;

    public TribeMemberDisconnectEvent(TribeMember member, Location.Game game) {
        super(member);
        this.game = game;
    }

    public Location.Game getGame() {
        return game;
    }
}
