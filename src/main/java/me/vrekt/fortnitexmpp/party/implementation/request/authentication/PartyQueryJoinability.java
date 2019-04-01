package me.vrekt.fortnitexmpp.party.implementation.request.authentication;

import me.vrekt.fortnitexmpp.party.implementation.DefaultParty;
import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

public final class PartyQueryJoinability implements PartyRequest {

    private final String payload;

    /**
     * Initialize this request
     *
     * @param partyId             the ID of the party
     * @param accessKey           the access key used to join the party
     * @param crossplayPreference the crossplay preference, 1 == OptedIn, 0 = ?? 2 = ??
     */
    public PartyQueryJoinability(final String partyId, final String accessKey, final int crossplayPreference) {
        final var payload = Json.createObjectBuilder()
                .add("partyId", partyId)
                .add("accessKey", accessKey)
                .add("appid", "Fortnite")
                .add("buildid", String.valueOf(DefaultParty.buildId))
                .add("joinData", Json.createObjectBuilder()
                        .add("Rev", RequestBuilder.getRevisionFor(PartyType.PARTY_QUERY_JOINABILITY))
                        .add("Attrs", Json.createObjectBuilder()
                                .add("CrossplayPreference_i", crossplayPreference).build()).build());
        this.payload = RequestBuilder.buildRequest(payload.build(), PartyType.PARTY_QUERY_JOINABILITY).toString();
    }

    /**
     * Initialize this request
     *
     * @param party               the party this request belongs to
     * @param crossplayPreference the crossplay preference, 1 == OptedIn, 0 = ?? 2 = ??
     */
    public PartyQueryJoinability(final Party party, final int crossplayPreference) {
        this(party.partyId(), party.accessKey(), crossplayPreference);
    }

    @Override
    public String payload() {
        return payload;
    }
}
