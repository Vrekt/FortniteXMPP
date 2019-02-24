package me.vrekt.fortnitexmpp.implementation.party.event.member;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;

public final class PartyMemberPromotedEvent {

    private final Party party;
    private final Jid from;
    private String promotedUserId;
    private boolean fromLeaderLeaving;

    public PartyMemberPromotedEvent(Party party, JsonObject payload, Jid from) {
        this.party = party;
        this.from = from;

        JsonUtility.getString("promotedMemberUserId", payload).ifPresent(id -> promotedUserId = id);
        JsonUtility.getBoolean("fromLeaderLeaving", payload).ifPresent(fromLeaderLeaving -> this.fromLeaderLeaving = fromLeaderLeaving);
    }

    public Party getParty() {
        return party;
    }

    public Jid getFrom() {
        return from;
    }

    public String getPromotedUserId() {
        return promotedUserId;
    }

    public boolean isFromLeaderLeaving() {
        return fromLeaderLeaving;
    }
}
