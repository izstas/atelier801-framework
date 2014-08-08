package com.atelier801.transformice.event;

import com.atelier801.transformice.TribeMember;

/**
 * This event gets triggered when a new member joins the tribe.
 */
public class TribeMemberJoinEvent extends TribeMemberEvent {
    public TribeMemberJoinEvent(TribeMember member) {
        super(member);
    }
}
