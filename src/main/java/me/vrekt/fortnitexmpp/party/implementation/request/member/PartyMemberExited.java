package me.vrekt.fortnitexmpp.party.implementation.request.member;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

public final class PartyMemberExited implements PartyRequest {

    private final String payload;

    public PartyMemberExited(final Party party, final String accountId, final boolean wasKicked) {
        this.payload = RequestBuilder.buildRequest(Json.createObjectBuilder()
                .add("partyId", party.partyId())
                .add("memberId", accountId)
                .add("wasKicked", wasKicked).build(), PartyType.PARTY_MEMBER_EXITED).toString();
    }

    @Override
    public String payload() {
        return payload;
    }
}
