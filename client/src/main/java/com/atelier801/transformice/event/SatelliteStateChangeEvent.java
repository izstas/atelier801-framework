package com.atelier801.transformice.event;

import com.atelier801.transformice.Transformice;

/**
 * This event gets triggered when the satellite Transformice connection changes state.
 */
public class SatelliteStateChangeEvent extends Event {
    private final Transformice.SatelliteState state;

    public SatelliteStateChangeEvent(Transformice.SatelliteState state) {
        this.state = state;
    }

    public Transformice.SatelliteState getState() {
        return state;
    }
}
