package me.vrekt.fortnitexmpp.implementation.party.packet.authentication.client;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

/**
 * A packet used for acknowledging you can join a party.
 * Usually a response is sent back with the party ID.
 */
public final class PacketPartyJoinAcknowledged implements PartyPacket {

    private final Jid to;
    private final String payload;

    public PacketPartyJoinAcknowledged(Party party) {
        final var type = PartyPacketType.PARTY_JOIN_ACKNOWLEDGED;
        this.to = party.getFrom();

        var payload = Json.createObjectBuilder().add("partyId", party.getId()).build();
        this.payload = PacketBuilder.buildPacket(payload, type).toString();
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
