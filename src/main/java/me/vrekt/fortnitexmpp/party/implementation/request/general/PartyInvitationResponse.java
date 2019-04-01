package me.vrekt.fortnitexmpp.party.implementation.request.general;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

public final class PartyInvitationResponse implements PartyRequest {

    private final String payload;

    /**
     * Initialize this request
     *
     * @param party    the party
     * @param response the response type t
     */
    public PartyInvitationResponse(final Party party, final Response response) {
        this.payload = RequestBuilder.buildRequest(
                Json.createObjectBuilder()
                        .add("partyId", party.partyId())
                        .add("response", response.getCode()).build(), PartyType.PARTY_INVITATION_RESPONSE).toString();
    }

    @Override
    public String payload() {
        return payload;
    }

    public enum Response {
        ACCEPTED(1), REJECTED(2), UNKNOWN(3);

        private final int code;

        Response(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

}
