package me.vrekt.fortnitexmpp.party.implementation.data;

import me.vrekt.fortnitexmpp.utility.JsonUtility;

import javax.json.JsonObject;

public final class ImmutablePartyData {

    private String customMatchKey, playlistName, tournamentId, eventWindowId, regionId, sessionId, sessionKey, partyInviteRestriction, partyType;
    private boolean allowJoinInProgress, squadFill, partyIsJoinedInProgress, onlyLeaderFriendsCanJoin;

    /**
     * Attempts to create a new instance from the provided {@code payload}
     *
     * @param payload the payload sent
     * @return a new {@link ImmutablePartyData} instance or {@code null} if the payload is invalid.
     */
    public static ImmutablePartyData adaptFrom(final JsonObject payload) {
        final var attributes = JsonUtility.getObject("payload", "Attrs", payload).orElse(null);
        if (attributes == null) return null;
        return new ImmutablePartyData(attributes);
    }

    /**
     * Initialize this instance
     *
     * @param payload the payload
     */
    private ImmutablePartyData(final JsonObject payload) {
        // basic stuff, custom matchmaking and the session ID if in game.
        JsonUtility.getString("PrimaryGameSessionId_s", payload).ifPresent(sessionId -> this.sessionId = sessionId);
        JsonUtility.getString("CustomMatchKey_s", payload).ifPresent(customMatchKey -> this.customMatchKey = customMatchKey);

        // playlist information
        JsonUtility.getObject("PlaylistData_j", "PlaylistData", payload).ifPresent(object -> {
            JsonUtility.getString("playlistName", object).ifPresent(playlistName -> this.playlistName = playlistName);
            JsonUtility.getString("tournamentId", object).ifPresent(tournamentId -> this.tournamentId = tournamentId);
            JsonUtility.getString("eventWindowId", object).ifPresent(eventWindowId -> this.eventWindowId = eventWindowId);
            JsonUtility.getString("regionId", object).ifPresent(regionId -> this.regionId = regionId);
        });

        // basic party information + session key used for endpoints.
        JsonUtility.getBoolean("AllowJoinInProgress_b", payload).ifPresent(allowJoinInProgress -> this.allowJoinInProgress = allowJoinInProgress);
        JsonUtility.getBoolean("AthenaSquadFill_b", payload).ifPresent(squadFill -> this.squadFill = squadFill);
        JsonUtility.getBoolean("PartyIsJoinedInProgress_b", payload).ifPresent(partyIsJoinedInProgress -> this.partyIsJoinedInProgress = partyIsJoinedInProgress);
        JsonUtility.getString("GameSessionKey_s", payload).ifPresent(sessionKey -> this.sessionKey = sessionKey);

        // privacy information
        JsonUtility.getObject("PrivacySettings_j", "PrivacySettings", payload).ifPresent(object -> {
            JsonUtility.getString("partyType", object).ifPresent(partyType -> this.partyType = partyType);
            JsonUtility.getString("partyInviteRestriction", object).ifPresent(partyInviteRestriction -> this.partyInviteRestriction = partyInviteRestriction);
            JsonUtility.getBoolean("bOnlyLeaderFriendsCanJoin", object).ifPresent(onlyLeaderFriendsCanJoin -> this.onlyLeaderFriendsCanJoin = onlyLeaderFriendsCanJoin);
        });
    }

    public String getCustomMatchKey() {
        return customMatchKey;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public String getEventWindowId() {
        return eventWindowId;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getPartyInviteRestriction() {
        return partyInviteRestriction;
    }

    public String getPartyType() {
        return partyType;
    }

    public boolean allowJoinInProgress() {
        return allowJoinInProgress;
    }

    public boolean squadFill() {
        return squadFill;
    }

    public boolean partyIsJoinedInProgress() {
        return partyIsJoinedInProgress;
    }

    public boolean onlyLeaderFriendsCanJoin() {
        return onlyLeaderFriendsCanJoin;
    }
}
