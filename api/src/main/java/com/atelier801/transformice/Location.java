package com.atelier801.transformice;

import lombok.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a game + room pair.
 */
@Value
public final class Location {
    private final Game game;
    private final String room;

    @Override
    public String toString() {
        return room + " on " + game;
    }


    /**
     * A enum-like class representing Atelier 801 games.
     */
    public static final class Game {
        private static final Map<Integer, Game> byId = new HashMap<>();

        public static final Game NONE = new Game(1, "None");
        public static final Game TRANSFORMICE = new Game(4, "Transformice");
        public static final Game FORTORESSE = new Game(6, "Fortoresse");
        public static final Game BOUBOUM = new Game(7, "Bouboum");
        public static final Game NEKODANCER = new Game(15, "Nekodancer");
        public static final Game DEADMAZE = new Game(17, "Deadmaze");


        private final int id;
        private final String name;

        private Game(int id) {
            this(id, "#" + id + "#");
        }

        private Game(int id, String name) {
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


        public static synchronized Game valueOf(int id) {
            Game value = byId.get(id);
            if (value == null) {
                value = new Game(id);
            }

            return value;
        }

        public static Collection<Game> values() {
            return Collections.unmodifiableCollection(byId.values());
        }
    }
}
