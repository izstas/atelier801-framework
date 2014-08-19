package com.atelier801.transformice.event;

import com.atelier801.transformice.TribeMember;

/**
 * This event gets triggered when tribe house map changes.
 */
public class TribeHouseMapChangeEvent extends Event {
    private final TribeMember changer;

    public TribeHouseMapChangeEvent(TribeMember changer) {
        this.changer = changer;
    }

    public TribeMember getChanger() {
        return changer;
    }
}
