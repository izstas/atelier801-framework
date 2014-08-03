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

    void changeCommunity(Community community);
    Observable<LoginEvent> logIn(String username, Optional<String> password, Optional<String> room);


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
}
