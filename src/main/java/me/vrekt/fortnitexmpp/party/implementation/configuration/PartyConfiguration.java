package me.vrekt.fortnitexmpp.party.implementation.configuration;

public final class PartyConfiguration {

    private final PrivacySetting setting;
    private final long presencePermissions;
    private final int invitePermissions, partyFlags, notAcceptingMembersReason, maxMembers;

    /**
     * Initialize this configuration
     *
     * @param setting             the privacy setting
     * @param maxMembers          the max amount of members allowed in the party
     * @param presencePermissions the permissions used for presence.
     */
    public PartyConfiguration(final PrivacySetting setting, final int maxMembers, final long presencePermissions) {
        this.setting = setting;
        this.presencePermissions = presencePermissions;
        this.maxMembers = maxMembers;

        this.invitePermissions = setting.invitePermissions();
        this.notAcceptingMembersReason = setting.notAcceptingMembersReason();
        this.partyFlags = setting.partyFlags();
    }

    /**
     * Initialize this configuration
     *
     * @param invitePermissions         the invite permissions for the party
     * @param partyFlags                the "flags" of the party
     * @param notAcceptingMembersReason the reason members are not being accepted (7 == private)
     * @param maxMembers                the max amount of members allowed in a party
     * @param presencePermissions       the permissions used for presence.
     */
    public PartyConfiguration(final int invitePermissions, final int partyFlags, final int notAcceptingMembersReason, final int maxMembers, final long presencePermissions) {
        this(PrivacySetting.typeOf(invitePermissions, partyFlags, notAcceptingMembersReason), maxMembers, presencePermissions);
    }

    public PrivacySetting settings() {
        return setting;
    }

    public long presencePermissions() {
        return presencePermissions;
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

    public int maxMembers() {
        return maxMembers;
    }
}
