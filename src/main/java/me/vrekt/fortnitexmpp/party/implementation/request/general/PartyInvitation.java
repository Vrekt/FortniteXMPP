package me.vrekt.fortnitexmpp.party.implementation.request.general;

import me.vrekt.fortnitexmpp.party.implementation.DefaultParty;
import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

public final class PartyInvitation implements PartyRequest {

    private final String payload;

    /**
     * Initialize this request
     *
     * @param partyId         the ID of the party
     * @param accessKey       the access key used to join the party
     * @param yourDisplayName the display name of the connected account
     */
    public PartyInvitation(final String partyId, final String accessKey, final String yourDisplayName) {
        final var payload = Json.createObjectBuilder()
                .add("partyId", partyId)
                .add("partyTypeId", Party.TYPE_ID)
                .add("displayName", yourDisplayName)
                .add("accessKey", accessKey)
                .add("appId", "Fortnite")
                .add("buildId", String.valueOf(DefaultParty.buildId));
        this.payload = RequestBuilder.buildRequest(payload.build(), PartyType.PARTY_INVITATION).toString();
    }

    /**
     * Initialize this request
     *
     * @param party           the party
     * @param yourDisplayName the display name of the connected account
     */
    public PartyInvitation(final Party party, final String yourDisplayName) {
        this(party.partyId(), party.accessKey(), yourDisplayName);
    }

    @Override
    public String payload() {
        return payload;
    }
}
