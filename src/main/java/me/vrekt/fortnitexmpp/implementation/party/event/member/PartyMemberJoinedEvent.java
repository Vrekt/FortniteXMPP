package me.vrekt.fortnitexmpp.implementation.party.event.member;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.member.PartyMember;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;

public final class PartyMemberJoinedEvent {

    private final PartyMember member;
    private final Party party;
    private final Jid from;

    public PartyMemberJoinedEvent(Party party, JsonObject payload, Jid from) {
        this.party = party;
        this.from = from;
        this.member = PartyMember.newBuilder(PartyMember.Type.WITH_PAYLOAD).setMemberData(payload.getJsonObject("member")).build();
        party.addMember(member);
    }

    public PartyMember getMember() {
        return member;
    }

    public Jid getFrom() {
        return from;
    }

    public Party getParty() {
        return party;
    }
}
