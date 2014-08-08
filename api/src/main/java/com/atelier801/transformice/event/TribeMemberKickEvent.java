package com.atelier801.transformice.event;

import com.atelier801.transformice.TribeMember;

/**
 * This event gets triggered when a member gets kicked from the tribe.
 */
public class TribeMemberKickEvent extends TribeMemberEvent {
    private final TribeMember kicker;

    public TribeMemberKickEvent(TribeMember member, TribeMember kicker) {
        super(member);
        this.kicker = kicker;
    }

    public TribeMember getKicker() {
        return kicker;
    }
}
