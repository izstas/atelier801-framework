package com.atelier801.transformice;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A enum-like class representing Transformice map categories.
 */
public final class RoomMapCategory {
    private static final Map<Integer, RoomMapCategory> byId = new HashMap<>();

    /**
     * Not a real category: Vanilla maps <strong>without XML</strong>
     */
    public static final RoomMapCategory NON_XML_VANILLA = new RoomMapCategory(-1, "Non-XML Vanilla");

    /**
     * Not a real category: Vanilla maps <strong>with XML</strong>
     */
    public static final RoomMapCategory VANILLA = new RoomMapCategory(-2, "Vanilla");

    public static final RoomMapCategory UNPROTECTED = new RoomMapCategory(0, "Unprotected");
    public static final RoomMapCategory PROTECTED = new RoomMapCategory(1, "Protected");
    public static final RoomMapCategory PRIME_BOOTCAMP = new RoomMapCategory(3, "Prime Bootcamp");
    public static final RoomMapCategory SHAMAN = new RoomMapCategory(4, "Shaman");
    public static final RoomMapCategory ART = new RoomMapCategory(5, "Art");
    public static final RoomMapCategory MECHANISM = new RoomMapCategory(6, "Mechanism");
    public static final RoomMapCategory NO_SHAMAN = new RoomMapCategory(7, "No Shaman");
    public static final RoomMapCategory DUAL_SHAMAN = new RoomMapCategory(8, "Dual Shaman");
    public static final RoomMapCategory MISCELLANEOUS = new RoomMapCategory(9, "Miscellaneous");
    public static final RoomMapCategory SURVIVOR = new RoomMapCategory(10, "Survivor");
    public static final RoomMapCategory VAMPIRE = new RoomMapCategory(11, "Vampire");
    public static final RoomMapCategory BOOTCAMP = new RoomMapCategory(13, "Bootcamp");
    public static final RoomMapCategory RACING = new RoomMapCategory(17, "Racing");
    public static final RoomMapCategory DEFILANTE = new RoomMapCategory(18, "Defilante");
    public static final RoomMapCategory MUSIC = new RoomMapCategory(19, "Music");
    public static final RoomMapCategory TRIBE_HOUSE = new RoomMapCategory(22, "Tribe House");
    public static final RoomMapCategory DELETED = new RoomMapCategory(44, "Deleted");


    private final int id;
    private final String name;

    private RoomMapCategory(int id) {
        this(id, "P" + id);
    }

    private RoomMapCategory(int id, String name) {
        this.id = id;
        this.name = name;

        byId.put(id, this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + (id >= 0 ? " (P" + id + ")" : "");
    }


    public static RoomMapCategory valueOf(int id) {
        return byId.computeIfAbsent(id, RoomMapCategory::new);
    }

    public static Collection<RoomMapCategory> values() {
        return Collections.unmodifiableCollection(byId.values());
    }
}
