package me.vrekt.fortnitexmpp.party.implementation.request.authentication;

import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

public final class PartyQueryJoinabilityResponse implements PartyRequest {

    private final String payload;

    /**
     * Initialize this request
     *
     * @param partyId       the ID of the party
     * @param allowedToJoin {@code true} if the recipient is allowed to join.
     */
    public PartyQueryJoinabilityResponse(final String partyId, final boolean allowedToJoin) {
        final var payload = Json.createObjectBuilder()
                .add("partyId", partyId)
                .add("isJoinable", allowedToJoin)
                .add("rejectionType", 0)
                .add("resultParam", "");
        this.payload = RequestBuilder.buildRequest(payload.build(), PartyType.PARTY_QUERY_JOINABILITY_RESPONSE).toString();
    }

    @Override
    public String payload() {
        return payload;
    }
}
