package me.vrekt.fortnitexmpp.party.implementation.error;

public enum ErrorType {

    NO_RESPONSE(-91), QUERY_JOINABILITY_MALFORMED_OR_NOT_ALLOWED(0);

    private final int code;

    ErrorType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
