package me.vrekt.fortnitexmpp.party.implementation.request.member;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

public final class PartyMemberPromoted implements PartyRequest {

    private final String payload;

    /**
     * Initialize this request
     *
     * @param party                the party
     * @param promotedMemberId     the user who was promoted
     * @param wasFromLeaderLeaving {@code true} if the user was promoted because the leader left.
     */
    public PartyMemberPromoted(final Party party, final String promotedMemberId, final boolean wasFromLeaderLeaving) {
        this.payload = RequestBuilder.buildRequest(Json.createObjectBuilder()
                .add("partyId", party.partyId())
                .add("promotedMemberUserId", promotedMemberId)
                .add("fromLeaderLeaving", wasFromLeaderLeaving).build(), PartyType.PARTY_MEMBER_PROMOTED).toString();
    }

    /**
     * Initialize this request
     *
     * @param partyId              the ID of the party
     * @param promotedMemberId     the user who was promoted
     * @param wasFromLeaderLeaving {@code true} if the user was promoted because the leader left.
     */
    public PartyMemberPromoted(final String partyId, final String promotedMemberId, final boolean wasFromLeaderLeaving) {
        this.payload = RequestBuilder.buildRequest(Json.createObjectBuilder()
                .add("partyId", partyId)
                .add("promotedMemberUserId", promotedMemberId)
                .add("fromLeaderLeaving", wasFromLeaderLeaving).build(), PartyType.PARTY_MEMBER_PROMOTED).toString();
    }

    @Override
    public String payload() {
        return payload;
    }
}
