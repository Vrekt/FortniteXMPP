package me.vrekt.fortnitexmpp.exception;

public final class XMPPAuthenticationException extends Exception {

    /**
     * An exception used for notifying when an attempt to connect to the XMPP service failed.
     */
    public XMPPAuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
