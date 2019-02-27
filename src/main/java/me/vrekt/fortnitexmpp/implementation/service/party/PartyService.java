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

    /**
     * Adds a party listener
     *
     * @param listener the listener
     */
    void addPartyListener(PartyListener listener);

    /**
     * Removes a party listener
     *
     * @param listener the listener
     */
    void removePartyListener(PartyListener listener);

    /**
     * Sends a packet, this method uses {@code PartyPacket#getFrom()} for recipient.
     *
     * @param partyPacket the packet
     */
    void sendPacket(PartyPacket partyPacket);

    /**
     * Sends a packet
     *
     * @param partyPacket the packet
     * @param to          who it is going to
     */
    void sendPacketTo(PartyPacket partyPacket, Jid to);

    /**
     * Sends a packet to all members in the collection. This method will ignore the current connections JID if present in the collection.
     * This is for convenience since you can just store all members in a collection and not worry about excluding yourself in the collection
     *
     * @param partyPacket the packet
     * @param members     the list of members
     */
    void sendPacketToAll(PartyPacket partyPacket, Collection<PartyMember> members);

    /**
     * Internally there is a party instance kept updated and only will get cleared when a new packet is received with a ID and key.
     *
     * @return the internal party context
     */
    Optional<Party> getPartyContext();

}
