package me.vrekt.fortnitexmpp.implementation.party.event.join;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.member.PartyMember;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;

public final class PartyJoinRequestEvent {

    private final Party party;
    private final Jid from;
    private PartyMember member;

    public PartyJoinRequestEvent(Party party, JsonObject payload, Jid from) {
        this.party = party;
        this.from = from;

        var accountId = from.getLocalpartOrThrow().toString();
        var resource = from.getResourceOrEmpty().toString();
        JsonUtility.getString("displayName", payload).ifPresent(displayName -> {
            member = PartyMember.newBuilder(PartyMember.Type.NORMAL)
                    .setAccountId(accountId).setResource(resource).setDisplayName(displayName).build();
        });
    }

    public Party getParty() {
        return party;
    }

    public Jid getFrom() {
        return from;
    }

    public PartyMember getMember() {
        return member;
    }
}
