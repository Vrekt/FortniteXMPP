package me.vrekt.fortnitexmpp.implementation.party.event.invite;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;

public final class PartyInvitationResponseEvent {

    private final Party party;
    private final Jid from;
    private Response response;

    public PartyInvitationResponseEvent(Party party, JsonObject payload, Jid from) {
        this.party = party;
        this.from = from;
        try {
            JsonUtility.getInt("response", payload).ifPresent(responseCode -> this.response = responseCode == 1 ? Response.ACCEPTED : Response.REJECTED);
        } catch (Exception exception) {
            System.err.println("Failed to parse message JSON for PartyQueryJoinabilityEvent!");
            exception.printStackTrace();
        }
    }

    public Party getParty() {
        return party;
    }

    public Response getResponse() {
        return response;
    }

    public Jid getFrom() {
        return from;
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
