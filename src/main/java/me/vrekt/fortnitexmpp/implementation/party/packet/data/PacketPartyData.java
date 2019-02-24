package me.vrekt.fortnitexmpp.implementation.party.packet.data;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

/**
 * A packet used for sending party data.
 */
public final class PacketPartyData implements PartyPacket {

    private final String payload;
    private final Jid to;

    public PacketPartyData(Party party, PartyData data) {
        this.payload = PacketBuilder.buildPacketDoublePayload(party.getId(), data.getPayload(), PartyPacketType.PARTY_DATA).toString();
        this.to = party.getFrom();
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public Jid getTo() {
        return to;
    }

}
