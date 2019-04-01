package me.vrekt.fortnitexmpp.party.implementation.request.member;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.member.PartyMember;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

public final class PartyMemberJoined implements PartyRequest {

    private final String payload;

    /**
     * Initialize this request
     *
     * @param party        the party
     * @param accountId    the ID of the account
     * @param xmppResource the XMPP resource of the account
     * @param displayName  the display name of the account
     */
    public PartyMemberJoined(final Party party, final String accountId, final String xmppResource, final String displayName) {
        final var payload = Json.createObjectBuilder()
                .add("partyId", party.partyId())
                .add("member", Json.createObjectBuilder()
                        .add("userId", accountId)
                        .add("xmppResource", xmppResource)
                        .add("displayName", displayName).build()).build();
        this.payload = RequestBuilder.buildRequest(payload, PartyType.PARTY_MEMBER_JOINED).toString();
    }

    /**
     * Initialize this request
     *
     * @param party  the party
     * @param member the member who joined
     */
    public PartyMemberJoined(final Party party, final PartyMember member) {
        this(party, member.accountId(), member.resource(), member.displayName());
    }

    @Override
    public String payload() {
        return payload;
    }
}
