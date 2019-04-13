package me.vrekt.fortnitexmpp.party.implementation.request.member.status;

public enum PartyMemberStatus {

    READY("Ready"), NOT_READY("NotReady"), SITTING_OUT("SittingOut");

    private final String name;

    PartyMemberStatus(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
