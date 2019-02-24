package me.vrekt.fortnitexmpp.implementation.party.event.join;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.member.PartyMember;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

public final class PartyJoinRequestApprovedEvent {

    private final Party party;
    private final Jid from;
    private final List<PartyMember> members = new ArrayList<>();

    public PartyJoinRequestApprovedEvent(Party party, JsonObject payload, Jid from) {
        this.party = party;
        this.from = from;

        JsonUtility.getArray("members", payload).ifPresent(array -> array.forEach(value ->
                members.add(PartyMember.newBuilder(PartyMember.Type.WITH_PAYLOAD).setMemberData(value.asJsonObject()).build())));
    }

    public Party getParty() {
        return party;
    }

    public Jid getFrom() {
        return from;
    }

    public List<PartyMember> getMembers() {
        return members;
    }
}
