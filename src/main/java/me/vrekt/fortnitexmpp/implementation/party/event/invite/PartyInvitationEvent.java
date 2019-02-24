package me.vrekt.fortnitexmpp.implementation.party.event.invite;

import me.vrekt.fortnitexmpp.implementation.PlatformType;
import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.member.data.PartyMemberData;
import me.vrekt.fortnitexmpp.implementation.service.party.PartyJoinData;
import org.jxmpp.jid.Jid;

import java.util.function.Consumer;

public final class PartyInvitationEvent {

    private final Party party;
    private final Consumer<PartyJoinData> acceptHandler;
    private final Jid from;

    public PartyInvitationEvent(Party party, Consumer<PartyJoinData> acceptHandler, Jid from) {
        this.party = party;
        this.acceptHandler = acceptHandler;
        this.from = from;
    }

    public Party getParty() {
        return party;
    }

    public Jid getFrom() {
        return from;
    }

    public void accept(PlatformType platformType, PartyMemberData data) {
        acceptHandler.accept(new PartyJoinData(party, platformType, data));
    }

}
