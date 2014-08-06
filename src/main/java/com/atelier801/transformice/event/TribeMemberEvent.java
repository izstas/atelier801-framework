package com.atelier801.transformice.event;

import com.atelier801.transformice.TribeMember;

/**
 * A base class for events related to some tribe member.
 */
public abstract class TribeMemberEvent extends Event {
    private final TribeMember member;

    public TribeMemberEvent(TribeMember member) {
        this.member = member;
    }

    public TribeMember getMember() {
        return member;
    }
}
