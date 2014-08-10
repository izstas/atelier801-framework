package com.atelier801.transformice;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a tribe member.
 */
public interface TribeMember {
    String getName();
    TribeRank getRank();
    LocalDateTime getJoinTime();
    LocalDateTime getLastConnectTime();
    List<Location> getLocations();

    void changeRank(TribeRank rank);
}
