package com.atelier801.transformice.event;

import com.atelier801.transformice.Location;
import com.atelier801.transformice.TribeMember;

/**
 * This event gets triggered when a tribe member changes room.
 */
public class TribeMemberLocationChangeEvent extends TribeMemberEvent {
    private final Location location;

    public TribeMemberLocationChangeEvent(TribeMember member, Location location) {
        super(member);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
