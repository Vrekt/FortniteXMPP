package me.vrekt.fortnitexmpp.exception;

public final class FortniteAuthenticationException extends Exception {

    /**
     * An exception used for notifying an error when trying to authenticate an account.
     */
    public FortniteAuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
