package me.vrekt.fortnitexmpp.party.implementation.error;

/**
 * Rejection types.
 */
public enum RejectionType {

    NOT_A_MEMBER(-686087675), OTHER(-252);

    private final int code;

    RejectionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
