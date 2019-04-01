package me.vrekt.fortnitexmpp.party.implementation.request.authentication;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

/**
 * A acknowledgement request acknowledging you can join a party.
 */
public final class PartyJoinAcknowledged implements PartyRequest {

    private final String payload;

    /**
     * Initialize the request.
     *
     * @param party the party
     */
    public PartyJoinAcknowledged(final Party party) {
        final var type = PartyType.PARTY_JOIN_ACKNOWLEDGED;
        this.payload = RequestBuilder.buildRequest(
                Json.createObjectBuilder()
                        .add("partyId", party.partyId()).build(), type).toString();
    }

    @Override
    public String payload() {
        return payload;
    }
}
