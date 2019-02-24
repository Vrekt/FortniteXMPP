package me.vrekt.fortnitexmpp.implementation.party.packet.authentication.server;

import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

/**
 * A packet for responding to a join acknowledged packet.
 */
public final class PacketPartyJoinAcknowledgedResponse implements PartyPacket {

    private final String payload;
    private Jid to;

    public PacketPartyJoinAcknowledgedResponse(String partyId, Jid to) {
        final var type = PartyPacketType.PARTY_JOIN_ACKNOWLEDGED_RESPONSE;
        this.to = to;
        this.payload = PacketBuilder.buildPacket(Json.createObjectBuilder().add("partyId", partyId).build(), type).toString();
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
