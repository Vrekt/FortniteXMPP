package me.vrekt.fortnitexmpp.implementation.party.packet.member;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.member.data.PartyMemberData;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

/**
 * A packet for sending party member data.
 */
public final class PacketPartyMemberData implements PartyPacket {

    private final Jid to;
    private final String payload;

    public PacketPartyMemberData(Party party, PartyMemberData data) {
        final var type = PartyPacketType.PARTY_MEMBER_DATA;
        this.to = party.getFrom();

        this.payload = PacketBuilder.buildPacketDoublePayload(party.getId(), data.getPayload(), type).toString();
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
