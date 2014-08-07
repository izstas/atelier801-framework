package com.atelier801.transformice.event;

import com.atelier801.transformice.Transformice;

/**
 * This event gets triggered when the main Transformice connection changes state.
 */
public class StateChangeEvent extends Event {
    private final Transformice.State state;

    public StateChangeEvent(Transformice.State state) {
        this.state = state;
    }

    public Transformice.State getState() {
        return state;
    }
}
