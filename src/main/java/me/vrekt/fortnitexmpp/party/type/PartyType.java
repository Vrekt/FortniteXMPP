package me.vrekt.fortnitexmpp.party.type;

import java.util.List;

public enum PartyType {

    /**
     * An invitation to the party.
     */
    PARTY_INVITATION("com.epicgames.party.invitation"),
    /**
     * An invitation response, basically if somebody rejected or accepted.
     */
    PARTY_INVITATION_RESPONSE("com.epicgames.party.invitationresponse"),
    /**
     * A request to join a party.
     */
    PARTY_JOIN_REQUEST("com.epicgames.party.joinrequest"),
    /**
     * The previous request was accepted.
     */
    PARTY_JOIN_REQUEST_APPROVED("com.epicgames.party.joinrequest.approved"),
    /**
     * The previous request was rejected.
     */
    PARTY_JOIN_REQUEST_REJECTED("com.epicgames.party.joinrequest.rejected"),
    /**
     * The request was accepted, therefor the join must be acknowledged.
     */
    PARTY_JOIN_ACKNOWLEDGED("com.epicgames.party.joinacknowledged"),
    /**
     * The recipient has acknowledged the join.
     */
    PARTY_JOIN_ACKNOWLEDGED_RESPONSE("com.epicgames.party.joinacknowledged.response"),
    /**
     * Data about a party member, their cosmetics, challenge assist, etc.
     */
    PARTY_MEMBER_DATA("com.epicgames.party.memberdata"),
    /**
     * A party member has joined.
     */
    PARTY_MEMBER_JOINED("com.epicgames.party.memberjoined"),
    /**
     * A party member has exited.
     */
    PARTY_MEMBER_EXITED("com.epicgames.party.memberexited"),
    /**
     * A party member has been promoted.
     */
    PARTY_MEMBER_PROMOTED("com.epicgames.party.memberpromoted"),
    /**
     * Data about the party, privacy configuration, playlist, custom match key, etc.
     */
    PARTY_DATA("com.epicgames.party.data"),
    /**
     * Privacy settings
     */
    PARTY_CONFIGURATION("com.epicgames.party.updatepartyconfiguration"),
    /**
     * Check if a party is joinable, this is not required.
     */
    PARTY_QUERY_JOINABILITY("com.epicgames.party.queryjoinability"),
    /**
     * The response to a query
     */
    PARTY_QUERY_JOINABILITY_RESPONSE("com.epicgames.party.queryjoinability.response");

    private static final List<PartyType> TYPES = List.of(values());
    private final String name;

    PartyType(String name) {
        this.name = name;
    }

    public static PartyType typeOf(String action) {
        if (action == null) return null;
        return TYPES.stream().filter(type -> type.getName().equals(action)).findAny().orElse(null);
    }

    public String getName() {
        return name;
    }

}
