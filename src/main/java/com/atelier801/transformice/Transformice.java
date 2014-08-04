package com.atelier801.transformice;

import java.util.Optional;
import rx.Observable;

import com.atelier801.transformice.event.*;

/**
 * The main interface representing a connection to Transformice.
 */
public interface Transformice {
    Observable<Event> events();

    State getState();
    SatelliteState getSatelliteState();

    void close();

    void changeCommunity(Community community);
    Observable<LoginEvent> logIn(String username, Optional<String> password, Optional<String> room);

    String getClientMouseName();


    Tribe tribe();
    interface Tribe extends Chat {
    }

    Room room();
    interface Room extends Chat {
    }

    /**
     * Represents the state of main Transformice connection.
     */
    enum State {
        CONNECTING,
        CONNECTED,
        LOGGING_IN,
        LOGGED_IN,
        CLOSING,
        CLOSED
    }

    /**
     * Represents the state of satellite Transformice connection.
     */
    enum SatelliteState {
        CONNECTING,
        CONNECTED,
        CLOSING,
        CLOSED
    }
}
