package me.vrekt.fortnitexmpp.party.implementation.configuration;

public enum PrivacySetting {

    /**
     * A public party.
     */
    PUBLIC(32515, 7, 0),
    /**
     * A friends only party.
     */
    FRIENDS(32513, 7, 0),
    /**
     * A friends party that allows friends of friends, equivalent to a public party.
     */
    FRIENDS_ALLOW_FRIENDS_OF_FRIENDS(32515, 7, 0),
    /**
     * A private party
     */
    PRIVATE(32513, 6, 7),
    /**
     * A private party that allows friends of friends
     */
    PRIVATE_ALLOW_FRIENDS_OF_FRIENDS(32515, 6, 7);

    private final int invitePermissions, partyFlags, notAcceptingMembersReason;

    PrivacySetting(final int invitePermissions, final int partyFlags, final int notAcceptingMembersReason) {
        this.invitePermissions = invitePermissions;
        this.partyFlags = partyFlags;
        this.notAcceptingMembersReason = notAcceptingMembersReason;
    }

    public int invitePermissions() {
        return invitePermissions;
    }

    public int partyFlags() {
        return partyFlags;
    }

    public int notAcceptingMembersReason() {
        return notAcceptingMembersReason;
    }

    public static PrivacySetting typeOf(final int invitePermissions, final int partyFlags, final int notAcceptingMembersReason) {
        if (invitePermissions == 32515 && partyFlags == 7 && notAcceptingMembersReason == 0) return PrivacySetting.PUBLIC;
        if (invitePermissions == 32513 && partyFlags == 7 && notAcceptingMembersReason == 0) return PrivacySetting.FRIENDS;
        if (invitePermissions == 32513 && partyFlags == 6 && notAcceptingMembersReason == 7) return PrivacySetting.PRIVATE;
        if (invitePermissions == 32515 && partyFlags == 6 && notAcceptingMembersReason == 7) return PrivacySetting.PRIVATE_ALLOW_FRIENDS_OF_FRIENDS;
        return PrivacySetting.PUBLIC;
    }

}
