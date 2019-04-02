package me.vrekt.fortnitexmpp.party.implementation.request.authentication;

import me.vrekt.fortnitexmpp.party.implementation.error.RejectionType;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

public final class PartyJoinRequestRejected implements PartyRequest {

    private final String payload;

    /**
     * Initialize this request
     *
     * @param partyId     the ID of the party
     * @param type        the type of rejection
     * @param resultParam the result or {@code ""}
     */
    public PartyJoinRequestRejected(final String partyId, final RejectionType type, final String resultParam) {
        this.payload = RequestBuilder.buildRequest(Json.createObjectBuilder()
                .add("partyId", partyId)
                .add("rejectionType", type.getCode())
                .add("resultParam", resultParam == null ? "" : resultParam).build(), PartyType.PARTY_JOIN_REQUEST_REJECTED).toString();
    }

    @Override
    public String payload() {
        return payload;
    }
}
