package me.vrekt.fortnitexmpp.implementation.party.packet.configuration;

import me.vrekt.fortnitexmpp.implementation.party.packet.configuration.privacy.PrivacyType;

public final class PartyConfiguration {

    private final Permissions permissions;
    private final PrivacyType privacyType;
    private final boolean allowFriendsOfFriends;
    private final int maxMembers;
    private final long presencePermissions;

    public PartyConfiguration(PrivacyType privacyType, boolean allowFriendsOfFriends, int maxMembers, long presencePermissions) {
        switch (privacyType) {
            case FRIENDS:
                permissions = allowFriendsOfFriends ? Permissions.FRIENDS : Permissions.FRIENDS_WITHOUT_OTHER_FRIENDS;
                break;
            case PRIVATE:
                permissions = allowFriendsOfFriends ? Permissions.PRIVATE : Permissions.PRIVATE_WITHOUT_OTHER_FRIENDS;
                break;
            default:
                permissions = Permissions.PUBLIC;
                break;
        }
        this.privacyType = privacyType;
        this.allowFriendsOfFriends = allowFriendsOfFriends;
        this.maxMembers = maxMembers;
        this.presencePermissions = presencePermissions;
    }

    public PartyConfiguration(int invitePermissions, int partyFlags, int notAcceptingMembersReason, int maxMembers, long presencePermissions) {
        this(partyFlags == 7 && notAcceptingMembersReason == 0 && invitePermissions == 1 ? PrivacyType.PUBLIC :
                partyFlags == 6 && notAcceptingMembersReason == 7 ? PrivacyType.PRIVATE : PrivacyType.FRIENDS, invitePermissions != 1, maxMembers, presencePermissions);
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public PrivacyType getPrivacyType() {
        return privacyType;
    }

    public boolean allowFriendsOfFriends() {
        return allowFriendsOfFriends;
    }

    public long getPresencePermissions() {
        return presencePermissions;
    }

    /**
     * Used for permissions based on privacy
     */
    public enum Permissions {

        // TODO: Watch this, make sure they don't change!
        PUBLIC(3, 7, 0),
        FRIENDS_WITHOUT_OTHER_FRIENDS(1, 7, 0),
        FRIENDS(3, 7, 0),
        PRIVATE_WITHOUT_OTHER_FRIENDS(1, 6, 7),
        PRIVATE(3, 6, 7);

        private final int invitePermissions, partyFlags, notAcceptingMembersReason;

        Permissions(int invitePermissions, int partyFlags, int notAcceptingMembersReason) {
            this.invitePermissions = invitePermissions;
            this.partyFlags = partyFlags;
            this.notAcceptingMembersReason = notAcceptingMembersReason;
        }

        public int getInvitePermissions() {
            return invitePermissions;
        }

        public int getPartyFlags() {
            return partyFlags;
        }

        public int getNotAcceptingMembersReason() {
            return notAcceptingMembersReason;
        }
    }

}
