package me.vrekt.fortnitexmpp.party.implementation.error;

/**
 * Various error types given when trying to join a party.
 * Taken from the logs and from my own testing.
 */
public enum ErrorType {

    NO_RESPONSE(-91), REQUESTEE_NOT_LEADER(-92), REQUESTEE_NOT_MEMBER(-93), QUERY_FAILED(0);

    private final int code;

    ErrorType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
