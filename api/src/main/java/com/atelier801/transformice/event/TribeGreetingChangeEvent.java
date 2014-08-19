package com.atelier801.transformice.event;

import com.atelier801.transformice.TribeMember;

/**
 * This event gets triggered when tribe greeting changes.
 */
public class TribeGreetingChangeEvent extends Event {
    private final TribeMember changer;

    public TribeGreetingChangeEvent(TribeMember changer) {
        this.changer = changer;
    }

    public TribeMember getChanger() {
        return changer;
    }
}
