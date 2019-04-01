package me.vrekt.fortnitexmpp.party.implementation.request.general;

public enum InvitationResponse {

    // 3 seems to be given with an outdated build ID.
    ACCEPTED(1), REJECTED(2), OUTDATED_BUILD(3);

    private final int code;

    InvitationResponse(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
