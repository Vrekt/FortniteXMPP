package me.vrekt.fortnitexmpp.party.implementation.request.configuration;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.configuration.PartyConfiguration;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

public final class PartyUpdateConfiguration implements PartyRequest {

    private final String payload;

    /**
     * Initialize this request
     *
     * @param party         the party this request belongs to
     * @param configuration the current party configuration
     */
    public PartyUpdateConfiguration(final Party party, final PartyConfiguration configuration) {
        final var payload = Json.createObjectBuilder()
                .add("partyId", party.partyId())
                .add("presencePermissions", configuration.presencePermissions())
                .add("invitePermissions", configuration.invitePermissions())
                .add("partyFlags", configuration.partyFlags())
                .add("notAcceptingMembersReason", configuration.notAcceptingMembersReason())
                .add("maxMembers", configuration.maxMembers())
                .add("password", "")
                .add("accessKey", party.accessKey());
        this.payload = RequestBuilder.buildRequest(payload.build(), PartyType.PARTY_CONFIGURATION).toString();
    }

    @Override
    public String payload() {
        return payload;
    }
}
