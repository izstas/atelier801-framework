package com.atelier801.transformice.event;

import com.atelier801.transformice.TribeMember;
import com.atelier801.transformice.TribeRank;

/**
 * This event gets triggered when a member's rank gets changed.
 */
public class TribeMemberRankChangeEvent extends TribeMemberEvent {
    private final TribeRank rank;

    public TribeMemberRankChangeEvent(TribeMember member, TribeRank rank) {
        super(member);
        this.rank = rank;
    }

    public TribeRank getRank() {
        return rank;
    }
}
