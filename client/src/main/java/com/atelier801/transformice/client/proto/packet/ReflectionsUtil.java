package com.atelier801.transformice.client.proto.packet;

import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;

final class ReflectionsUtil {
    private static Map<String, Reflections> reflections = new HashMap<>();

    static Reflections forPackage(String pkg) {
        return reflections.computeIfAbsent(pkg, Reflections::new);
    }


    private ReflectionsUtil() {
        throw new AssertionError();
    }
}
