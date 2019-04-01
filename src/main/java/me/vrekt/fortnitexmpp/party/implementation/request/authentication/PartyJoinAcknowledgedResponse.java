package me.vrekt.fortnitexmpp.party.implementation.request.authentication;

import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

public final class PartyJoinAcknowledgedResponse implements PartyRequest {

    private final String payload;

    /**
     * Initialize this request.
     *
     * @param partyId the ID of the party
     */
    public PartyJoinAcknowledgedResponse(final String partyId) {
        this.payload = RequestBuilder.buildRequest(Json.createObjectBuilder()
                .add("partyId", partyId).build(), PartyType.PARTY_JOIN_ACKNOWLEDGED_RESPONSE).toString();
    }

    @Override
    public String payload() {
        return payload;
    }
}
