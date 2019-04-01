package me.vrekt.fortnitexmpp.party.implementation.request.authentication;

import me.vrekt.fortnitexmpp.party.implementation.DefaultParty;
import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

/**
 * A party join request.
 */
public final class PartyJoinRequest implements PartyRequest {

    private final String payload;

    /**
     * Initialize
     *
     * @param party               the party to you are requesting to join.
     * @param displayName         the display name of the current logged in account.
     * @param crossplayPreference the preference of crossplay {@code 1} to opt in.
     */
    public PartyJoinRequest(final Party party, final String displayName, final int crossplayPreference) {
        this.payload = RequestBuilder.buildRequest(
                Json.createObjectBuilder()
                        .add("partyId", party.partyId())
                        .add("displayName", displayName)
                        .add("accessKey", party.accessKey())
                        .add("appId", "Fortnite")
                        .add("buildId", String.valueOf(DefaultParty.buildId))
                        .add("joinData", Json.createObjectBuilder()
                                .add("Rev", RequestBuilder.getRevisionFor(PartyType.PARTY_JOIN_REQUEST))
                                .add("Attrs", Json.createObjectBuilder()
                                        .add("CrossplayPreference_i", crossplayPreference).build()).build()).build(), PartyType.PARTY_JOIN_REQUEST).toString();

    }

    /**
     * Initialize
     *
     * @param partyId             the ID of the party you are requesting to join.
     * @param accessKey           the access key of the party you are requesting to join.
     * @param displayName         the display name of the current logged in account.
     * @param crossplayPreference the preference of crossplay {@code 1} to opt in.
     */
    public PartyJoinRequest(final String partyId, final String accessKey, final String displayName, final int crossplayPreference) {
        this.payload = RequestBuilder.buildRequest(
                Json.createObjectBuilder()
                        .add("partyId", partyId)
                        .add("displayName", displayName)
                        .add("accessKey", accessKey)
                        .add("appId", "Fortnite")
                        .add("buildId", String.valueOf(DefaultParty.buildId))
                        .add("joinData", Json.createObjectBuilder()
                                .add("Rev", RequestBuilder.getRevisionFor(PartyType.PARTY_JOIN_REQUEST))
                                .add("Attrs", Json.createObjectBuilder()
                                        .add("CrossplayPreference_i", crossplayPreference).build()).build()).build(), PartyType.PARTY_JOIN_REQUEST).toString();
    }

    @Override
    public String payload() {
        return payload;
    }
}
