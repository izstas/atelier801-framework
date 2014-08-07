package com.atelier801.transformice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a game + room pair.
 */
public class Location {
    private final Game game;
    private final String room;

    public Location(Game game, String room) {
        this.game = game;
        this.room = room;
    }

    public Game getGame() {
        return game;
    }

    public String getRoom() {
        return room;
    }


    /**
     * A enum-like class representing Atelier 801 games.
     */
    public static final class Game {
        private static final Map<Integer, Game> byId = new HashMap<>();

        public static final Game TRANSFORMICE = new Game(4, "Transformice");
        public static final Game FORTORESSE = new Game(6, "Fortoresse");
        public static final Game BOUBOUM = new Game(7, "Bouboum");
        public static final Game NEKODANCER = new Game(15, "Nekodancer");


        private final int id;
        private final String name;

        private Game(int id) {
            this(id, "##" + id + "##");
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


        public static Game valueOf(int id) {
            return byId.computeIfAbsent(id, Game::new);
        }

        public static Collection<Game> values() {
            return byId.values();
        }
    }
}
