package com.atelier801.transformice.event;

/**
 * This event is triggered on a failed login attempt.
 */
public class LoginFailureEvent extends LoginEvent {
    private final Reason reason;

    public LoginFailureEvent(Reason reason) {
        this.reason = reason;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    public Reason getReason() {
        return reason;
    }


    public enum Reason {
        UNKNOWN,

        INVALID,
        ALREADY_CONNECTED
    }
}
