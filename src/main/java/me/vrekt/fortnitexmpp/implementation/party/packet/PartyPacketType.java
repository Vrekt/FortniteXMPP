package me.vrekt.fortnitexmpp.implementation.party.packet;

import java.util.List;

public enum PartyPacketType {

    PARTY_INVITATION("com.epicgames.party.invitation"),
    PARTY_INVITATION_RESPONSE("com.epicgames.party.invitationresponse"),
    PARTY_JOIN_REQUEST("com.epicgames.party.joinrequest"),
    PARTY_JOIN_REQUEST_APPROVED("com.epicgames.party.joinrequest.approved"),
    PARTY_JOIN_REQUEST_REJECTED("com.epicgames.party.joinrequest.rejected"),
    PARTY_JOIN_ACKNOWLEDGED("com.epicgames.party.joinacknowledged"),
    PARTY_JOIN_ACKNOWLEDGED_RESPONSE("com.epicgames.party.joinacknowledged.response"),
    PARTY_MEMBER_DATA("com.epicgames.party.memberdata"),
    PARTY_MEMBER_JOINED("com.epicgames.party.memberjoined"),
    PARTY_MEMBER_EXITED("com.epicgames.party.memberexited"),
    PARTY_MEMBER_PROMOTED("com.epicgames.party.memberpromoted"),
    PARTY_DATA("com.epicgames.party.data"),
    PARTY_CONFIGURATION("com.epicgames.party.updatepartyconfiguration"),
    PARTY_QUERY_JOINABILITY("com.epicgames.party.queryjoinability"),
    PARTY_QUERY_JOINABILITY_RESPONSE("com.epicgames.party.queryjoinability.response"),
    PARTY_UNKNOWN_TYPE("Unknown type!");

    private static final List<PartyPacketType> TYPES = List.of(values());
    private final String name;

    PartyPacketType(String name) {
        this.name = name;
    }

    public static PartyPacketType getFrom(String action) {
        return TYPES.stream().filter(type -> type.getName().equals(action)).findAny().orElse(PARTY_UNKNOWN_TYPE);
    }

    public String getName() {
        return name;
    }

}
