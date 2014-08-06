package com.atelier801.transformice.event;

import com.atelier801.transformice.Location;
import com.atelier801.transformice.TribeMember;

/**
 * This event gets triggered when a tribe member connects to a game.
 */
public class TribeMemberConnectEvent extends TribeMemberEvent {
    private final Location.Game game;

    public TribeMemberConnectEvent(TribeMember member, Location.Game game) {
        super(member);
        this.game = game;
    }

    public Location.Game getGame() {
        return game;
    }
}
