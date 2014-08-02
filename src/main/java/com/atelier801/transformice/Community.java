package com.atelier801.transformice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Community {
    public static final Community EN = new Community(0, "en");
    public static final Community FR = new Community(1, "fr");
    public static final Community RU = new Community(2, "ru");
    public static final Community BR = new Community(3, "br");
    public static final Community ES = new Community(4, "es");
    public static final Community CN = new Community(5, "cn");
    public static final Community TR = new Community(6, "tr");
    public static final Community VK = new Community(7, "vk");
    public static final Community PL = new Community(8, "pl");
    public static final Community HU = new Community(9, "hu");
    public static final Community NL = new Community(10, "nl");
    public static final Community RO = new Community(11, "ro");
    public static final Community ID = new Community(12, "id");
    public static final Community DE = new Community(13, "de");
    public static final Community E2 = new Community(14, "e2");
    public static final Community AR = new Community(15, "ar");
    public static final Community PH = new Community(16, "ph");
    public static final Community LT = new Community(17, "lt");
    public static final Community JP = new Community(18, "jp");
    public static final Community FI = new Community(20, "fi");
    public static final Community HE = new Community(26, "he");
    public static final Community IT = new Community(27, "it");

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

    public String getName() {
        return name;
    }


    public static Community valueOf(int id) {
        return byId.computeIfAbsent(id, Community::new);
    }

    public static Collection<Community> values() {
        return byId.values();
    }
}
