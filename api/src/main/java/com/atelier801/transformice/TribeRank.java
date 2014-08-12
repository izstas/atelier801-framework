package com.atelier801.transformice;

import java.util.Set;

/**
 * Represents a tribe rank.
 */
public interface TribeRank extends Comparable<TribeRank> {
    String getName();
    Set<TribePermission> getPermissions();
}
