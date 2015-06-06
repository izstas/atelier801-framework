package com.atelier801.transformice;

import java.time.Instant;
import java.util.Collections;
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
    Location getLocation();
    boolean isOnline();

    Observable<TribeMemberRankChangeEvent> changeRank(TribeRank rank);
    Observable<TribeMemberKickEvent> kick();

    @Deprecated
    default List<Location> getLocations() {
        return isOnline() ? Collections.singletonList(getLocation()) : Collections.emptyList();
    }
}
