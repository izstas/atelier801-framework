package com.atelier801.transformice.event;

/**
 * @see LoginSuccessEvent
 * @see LoginFailureEvent
 */
public abstract class LoginEvent extends Event {
    public abstract boolean isSuccess();
}
