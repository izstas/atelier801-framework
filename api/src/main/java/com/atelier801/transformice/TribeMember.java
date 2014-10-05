package com.atelier801.transformice;

import java.time.Instant;
import java.util.List;
import rx.Observable;

import com.atelier801.transformice.event.TribeMemberKickEvent;
import com.atelier801.transformice.event.TribeMemberRankChangeEvent;

/**
 * Represents a tribe member.
 */
public interface TribeMember {
    String getName();
    TribeRank getRank();
    Instant getJoiningTime();
    Instant getLastConnectionTime();
    List<Location> getLocations();

    Observable<TribeMemberRankChangeEvent> changeRank(TribeRank rank);
    Observable<TribeMemberKickEvent> kick();
}
