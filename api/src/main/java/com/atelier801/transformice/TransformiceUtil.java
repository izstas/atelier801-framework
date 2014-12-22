package com.atelier801.transformice;

/**
 * Miscellaneous utility methods.
 */
public final class TransformiceUtil {
    /**
     * Normalizes the mouse name, i.e. makes the first letter uppercase and the rest lowercase.
     * @param name the name
     * @return the normalized name
     */
    public static String normalizeMouseName(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (name.length() < 2) {
            throw new IllegalArgumentException("name must be at least 2 characters long");
        }

        if (name.charAt(0) == '*') {
            return name;
        }

        if (name.charAt(0) == '+') {
            return '+' + name.substring(1, 2).toUpperCase() + name.substring(2).toLowerCase();
        }

        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public static String escapeMessage(String message) {
        return message.replace("&", "&amp;").replace("<", "&lt;");
    }

    public static String unescapeMessage(String message) {
        return message.replace("&lt;", "<").replace("&amp;", "&");
    }


    private TransformiceUtil() {
        throw new AssertionError();
    }
}
