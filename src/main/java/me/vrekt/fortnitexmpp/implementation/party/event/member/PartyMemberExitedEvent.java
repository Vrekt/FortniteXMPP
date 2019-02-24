package me.vrekt.fortnitexmpp.implementation.party.event.member;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;

/**
 * Handles members exiting a party.
 */
public final class PartyMemberExitedEvent {

    private final Party party;
    private final Jid from;
    private String accountId;
    private boolean wasKicked;

    public PartyMemberExitedEvent(Party party, JsonObject payload, Jid from) {
        this.party = party;
        this.from = from;

        JsonUtility.getString("memberId", payload).ifPresent(memberId -> accountId = memberId);
        JsonUtility.getBoolean("wasKicked", payload).ifPresent(wasKicked -> this.wasKicked = wasKicked);
        party.removeMemberById(accountId);
    }

    public Party getParty() {
        return party;
    }

    public String getAccountId() {
        return accountId;
    }

    public boolean wasKicked() {
        return wasKicked;
    }

    public Jid getFrom() {
        return from;
    }
}
