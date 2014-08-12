package com.atelier801.transformice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A enum-like class representing tribe permissions.
 */
public final class TribePermission {
    private static final Map<Integer, TribePermission> byId = new HashMap<>();

    public static final TribePermission LEADER = new TribePermission(1, "Leader");
    public static final TribePermission CHANGE_GREETING = new TribePermission(2, "Can change the greeting message");
    public static final TribePermission CHANGE_RANKS = new TribePermission(3, "Can edit ranks");
    public static final TribePermission ASSIGN_RANKS = new TribePermission(4, "Can change the members' ranks");
    public static final TribePermission INVITE_MEMBERS = new TribePermission(5, "Can invite new members");
    public static final TribePermission KICK_MEMBERS = new TribePermission(6, "Can exclude members");
    public static final TribePermission PLAY_MUSIC = new TribePermission(7, "Can play music");
    public static final TribePermission CHANGE_HOUSE_MAP = new TribePermission(8, "Can change Tribe House map");
    public static final TribePermission PLAY_MAPS = new TribePermission(9, "Can load a map");


    private final int id;
    private final String description;

    private TribePermission(int id) {
        this(id, "#" + id + "#");
    }

    private TribePermission(int id, String description) {
        this.id = id;
        this.description = description;

        byId.put(id, this);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return description;
    }


    public static TribePermission valueOf(int id) {
        return byId.computeIfAbsent(id, TribePermission::new);
    }

    public static Collection<TribePermission> values() {
        return byId.values();
    }
}
