package com.atelier801.transformice.event;

import com.atelier801.transformice.Location;
import com.atelier801.transformice.TribeMember;

/**
 * This event gets triggered when a tribe member connects.
 */
public class TribeMemberConnectEvent extends TribeMemberEvent {
    public TribeMemberConnectEvent(TribeMember member) {
        super(member);
    }

    @Deprecated
    public Location.Game getGame() {
        return Location.Game.valueOf(1);
    }
}
