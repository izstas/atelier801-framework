package com.atelier801.transformice.event;

/**
 * This event gets triggered on a successful login attempt.
 */
public class LoginSuccessEvent extends LoginEvent {
    private final String mouseName;

    public LoginSuccessEvent(String mouseName) {
        this.mouseName = mouseName;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    public String getMouseName() {
        return mouseName;
    }
}
