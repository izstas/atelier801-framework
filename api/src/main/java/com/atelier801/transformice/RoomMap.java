package com.atelier801.transformice;

/**
 * Represents information about a Transformice map.
 */
public class RoomMap {
    private final int id;
    private final String xml;
    private final String author;
    private final RoomMapCategory category;

    public RoomMap(int id, String xml, String author, RoomMapCategory category) {
        this.id = id;
        this.xml = xml;
        this.author = author;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getXml() {
        return xml;
    }

    public String getAuthor() {
        return author;
    }

    public RoomMapCategory getCategory() {
        return category;
    }
}
