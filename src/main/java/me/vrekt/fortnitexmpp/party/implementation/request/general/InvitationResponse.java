package me.vrekt.fortnitexmpp.party.implementation.request.general;

/**
 * An enum representing the invite response.
 */
public enum InvitationResponse {

    // 0 seems to be given with an outdated build ID.
    // 3 is unknown.
    ACCEPTED(1), REJECTED(2), OUTDATED_BUILD_ID(0);

    private final int code;

    InvitationResponse(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
