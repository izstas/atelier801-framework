package com.atelier801.transformice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A enum-like class representing Transformice communities.
 */
public final class Community {
    public static final Community EN = new Community(0, "EN");
    public static final Community FR = new Community(1, "FR");
    public static final Community RU = new Community(2, "RU");
    public static final Community BR = new Community(3, "BR");
    public static final Community ES = new Community(4, "ES");
    public static final Community CN = new Community(5, "CN");
    public static final Community TR = new Community(6, "TR");
    public static final Community VK = new Community(7, "VK");
    public static final Community PL = new Community(8, "PL");
    public static final Community HU = new Community(9, "HU");
    public static final Community NL = new Community(10, "NL");
    public static final Community RO = new Community(11, "RO");
    public static final Community ID = new Community(12, "ID");
    public static final Community DE = new Community(13, "DE");
    public static final Community E2 = new Community(14, "E2");
    public static final Community AR = new Community(15, "AR");
    public static final Community PH = new Community(16, "PH");
    public static final Community LT = new Community(17, "LT");
    public static final Community JP = new Community(18, "JP");
    public static final Community FI = new Community(20, "FI");
    public static final Community HE = new Community(26, "HE");
    public static final Community IT = new Community(27, "IT");

    private static final Map<Integer, Community> byId = new HashMap<>();


    private final int id;
    private final String name;

    private Community(int id) {
        this(id, "##" + id + "##");
    }

    private Community(int id, String name) {
        this.id = id;
        this.name = name;

        byId.put(id, this);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }


    public static Community valueOf(int id) {
        return byId.computeIfAbsent(id, Community::new);
    }

    public static Collection<Community> values() {
        return byId.values();
    }
}
