package com.atelier801.transformice.event;

import com.atelier801.transformice.TribeMember;

/**
 * This event gets triggered when a member leaves the tribe.
 */
public class TribeMemberLeaveEvent extends TribeMemberEvent {
    public TribeMemberLeaveEvent(TribeMember member) {
        super(member);
    }
}
