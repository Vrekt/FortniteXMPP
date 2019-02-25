package me.vrekt.fortnitexmpp.implementation.party.packet.data;

import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Represents party data.
 * Use {@link Builder} to build data.
 */
public final class PartyData {

    private JsonObject payload;

    private PartyData(Builder builder) {
        if (builder.type == PartyDataType.MATCHMAKING_INFO) {
            initializeMatchmaking(builder);
        } else if (builder.type == PartyDataType.CHANGE_PLAYLIST) {
            initializePlaylist(builder);
        } else if (builder.type == PartyDataType.CUSTOM_KEY) {
            initializeCustomKey(builder);
        } else if (builder.type == PartyDataType.PRIVACY_SETTINGS) {
            initializePrivacySettings(builder);
        } else if (builder.type == PartyDataType.FULL) {
            initializeFull(builder);
        }
    }

    /**
     * Initializes a matchmaking state change
     *
     * @param builder the builder
     */
    private void initializeMatchmaking(Builder builder) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();

        payload.add("Rev", PacketBuilder.getRevForType(PartyPacketType.PARTY_DATA));
        attributes.add("PartyState_s", "BattleRoyaleMatchmaking");
        attributes.add("MatchmakingInfoString_s", builder.matchmakingInfoString);
        payload.add("Attrs", attributes.build());
        this.payload = payload.build();
    }

    /**
     * Initializes a playlist change
     *
     * @param builder the builder
     */
    private void initializePlaylist(Builder builder) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();

        payload.add("Rev", PacketBuilder.getRevForType(PartyPacketType.PARTY_DATA));
        attributes.add("PlaylistData_j", Json.createObjectBuilder().add("PlaylistData",
                Json.createObjectBuilder()
                        .add("playlistName", builder.playlistName)
                        .add("tournamentId", "")
                        .add("eventWindowId", "").build()).build());
        payload.add("Attrs", attributes.build());
        this.payload = payload.build();
    }

    /**
     * Initializes a custom key change
     *
     * @param builder the builder
     */
    private void initializeCustomKey(Builder builder) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();

        payload.add("Rev", PacketBuilder.getRevForType(PartyPacketType.PARTY_DATA));
        attributes.add("CustomMatchKey_s", builder.customKey);
        payload.add("Attrs", attributes.build());
        this.payload = payload.build();
    }

    /**
     * Initializes a privacy settings change
     *
     * @param builder the builder
     */
    private void initializePrivacySettings(Builder builder) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();
        var privacyData = Json.createObjectBuilder();

        payload.add("Rev", PacketBuilder.getRevForType(PartyPacketType.PARTY_DATA));

        privacyData.add("partyType", builder.partyType);
        privacyData.add("partyInviteRestriction", builder.partyInviteRestriction);
        privacyData.add("bOnlyLeaderFriendsCanJoin", builder.onlyLeaderFriendsCanJoin);
        attributes.add("PrivacySettings_j", Json.createObjectBuilder().add("PrivacySettings", privacyData.build()).build());
        payload.add("Attrs", attributes.build());
        this.payload = payload.build();
    }

    /**
     * Initializes a full data set
     *
     * @param builder the builder
     */
    private void initializeFull(Builder builder) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();
        var privacyData = Json.createObjectBuilder();

        payload.add("Rev", PacketBuilder.getRevForType(PartyPacketType.PARTY_DATA));

        attributes.add("PrimaryGameSessionId_s", "");
        attributes.add("PartyState_s", "BattleRoyaleView");
        attributes.add("LobbyConnectionStarted_b", false);
        attributes.add("MatchmakingResult_s", "NoResults");
        attributes.add("MatchmakingState_s", "NotMatchmaking");
        attributes.add("SessionIsCriticalMission_b", false);
        attributes.add("ZoneTileIndex_U", "-1");
        attributes.add("ZoneInstanceId_s", "");
        attributes.add("TheaterId_s", "");
        attributes.add("TileStates_j", Json.createObjectBuilder().add("TileStates", Json.createArrayBuilder().build()).build());
        attributes.add("MatchmakingInfoString_s", builder.matchmakingInfoString);
        attributes.add("CustomMatchKey_s", builder.customKey);
        attributes.add("PlaylistData_j", Json.createObjectBuilder().add("PlaylistData",
                Json.createObjectBuilder()
                        .add("playlistName", builder.playlistName)
                        .add("tournamentId", "")
                        .add("eventWindowId", "").build()).build());
        attributes.add("AllowJoinInProgress_b", false);
        attributes.add("LFGTime_s", "0001-01-01T00:00:00.000Z");
        attributes.add("AthenaSquadFill_b", true);
        attributes.add("PartyIsJoinedInProgress_b", false);
        attributes.add("GameSessionKey_s", "");

        privacyData.add("partyType", builder.partyType);
        privacyData.add("partyInviteRestriction", builder.partyInviteRestriction);
        privacyData.add("bOnlyLeaderFriendsCanJoin", builder.onlyLeaderFriendsCanJoin);
        attributes.add("PrivacySettings_j", Json.createObjectBuilder().add("PrivacySettings", privacyData.build()).build());
        attributes.add("PlatformSessions_j", Json.createObjectBuilder().add("PlatformSessions", Json.createArrayBuilder().build()).build());
        payload.add("Attrs", attributes.build());
        this.payload = payload.build();
    }

    public JsonObject getPayload() {
        return payload;
    }

    public static Builder newBuilder(PartyDataType type) {
        return new Builder(type);
    }

    public static final class Builder {

        private final PartyDataType type;
        private String matchmakingInfoString, playlistName, customKey;

        private String partyType, partyInviteRestriction;
        private boolean onlyLeaderFriendsCanJoin;

        private Builder(PartyDataType type) {
            this.type = type;
        }

        public Builder setMatchmakingInfoString(String matchmakingInfoString) {
            this.matchmakingInfoString = matchmakingInfoString;
            return this;
        }

        public Builder setPlaylistName(String playlistName) {
            this.playlistName = playlistName;
            return this;
        }

        public Builder setCustomKey(String customKey) {
            this.customKey = customKey;
            return this;
        }

        public Builder setPartyType(String partyType) {
            this.partyType = partyType;
            return this;
        }

        public Builder setPartyInviteRestriction(String partyInviteRestriction) {
            this.partyInviteRestriction = partyInviteRestriction;
            return this;
        }

        public Builder setOnlyLeaderFriendsCanJoin(boolean onlyLeaderFriendsCanJoin) {
            this.onlyLeaderFriendsCanJoin = onlyLeaderFriendsCanJoin;
            return this;
        }

        public PartyData build() {
            return new PartyData(this);
        }

    }

}
