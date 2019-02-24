package me.vrekt.fortnitexmpp.implementation.service.party;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.PartyListener;
import me.vrekt.fortnitexmpp.implementation.party.member.PartyMember;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.service.Service;
import org.jxmpp.jid.Jid;

import java.util.Collection;
import java.util.Optional;

public interface PartyService extends Service {

    void addPartyListener(PartyListener listener);

    void removePartyListener(PartyListener listener);

    void sendPacket(PartyPacket partyPacket);

    void sendPacketTo(PartyPacket partyPacket, Jid to);

    void sendPacketToAll(PartyPacket partyPacket, Collection<PartyMember> members);

    Optional<Party> getPartyContext();

}
