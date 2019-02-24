package me.vrekt.fortnitexmpp.implementation.party.event.data;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.packet.configuration.privacy.PrivacyType;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;

/**
 * This event is for party data
 * Party data contains more then whats included in this event, BUT
 * most of it seems to be related to PVE as-well and some of it is irrelevant
 */
public final class PartyDataEvent {

    private final Party party;
    private final Jid from;
    private final String payload;

    private String customMatchKey, playlistName, tournamentId, eventWindowId, sessionId, sessionKey, partyInviteRestriction;
    private PrivacyType type;
    private boolean allowJoinInProgress, squadFill, partyIsJoinedInProgress, onlyLeaderFriendsCanJoin;

    public PartyDataEvent(Party party, JsonObject payload, Jid from) {
        this.party = party;
        this.from = from;

        var data = payload.getJsonObject("payload").getJsonObject("Attrs");
        this.payload = data.toString();

        JsonUtility.getString("PrimaryGameSessionId_s", data).ifPresent(sessionId -> this.sessionId = sessionId);
        JsonUtility.getString("CustomMatchKey_s", data).ifPresent(customMatchKey -> this.customMatchKey = customMatchKey);

        JsonUtility.getObject("PlaylistData_j", data).ifPresent(playlistData_j -> {
            var playlistData = playlistData_j.getJsonObject("PlaylistData");
            JsonUtility.getString("playlistName", playlistData).ifPresent(playlistName -> this.playlistName = playlistName);
            JsonUtility.getString("tournamentId", playlistData).ifPresent(tournamentId -> this.tournamentId = tournamentId);
            JsonUtility.getString("eventWindowId", playlistData).ifPresent(eventWindowId -> this.eventWindowId = eventWindowId);
        });

        JsonUtility.getBoolean("AllowJoinInProgress_b", data).ifPresent(allowJoinInProgress -> this.allowJoinInProgress = allowJoinInProgress);
        JsonUtility.getBoolean("AthenaSquadFill_b", data).ifPresent(squadFill -> this.squadFill = squadFill);
        JsonUtility.getBoolean("PartyIsJoinedInProgress_b", data).ifPresent(partyIsJoinedInProgress -> this.partyIsJoinedInProgress = partyIsJoinedInProgress);
        JsonUtility.getString("GameSessionKey_s", data).ifPresent(sessionKey -> this.sessionKey = sessionKey);

        JsonUtility.getObject("PrivacySettings_j", data).ifPresent(privacySettings_j -> {
            var privacyData = privacySettings_j.getJsonObject("PrivacySettings");

            var partyType = privacyData.getString("partyType");
            partyInviteRestriction = privacyData.getString("partyInviteRestriction");
            onlyLeaderFriendsCanJoin = privacyData.getBoolean("bOnlyLeaderFriendsCanJoin");

            type = PrivacyType.of(partyType);
        });

    }

    public Party getParty() {
        return party;
    }

    public Jid getFrom() {
        return from;
    }

    public String getPayload() {
        return payload;
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

    public String getSessionId() {
        return sessionId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getPartyInviteRestriction() {
        return partyInviteRestriction;
    }

    public PrivacyType getType() {
        return type;
    }

    public boolean isAllowJoinInProgress() {
        return allowJoinInProgress;
    }

    public boolean isSquadFill() {
        return squadFill;
    }

    public boolean isPartyIsJoinedInProgress() {
        return partyIsJoinedInProgress;
    }

    public boolean isOnlyLeaderFriendsCanJoin() {
        return onlyLeaderFriendsCanJoin;
    }
}
