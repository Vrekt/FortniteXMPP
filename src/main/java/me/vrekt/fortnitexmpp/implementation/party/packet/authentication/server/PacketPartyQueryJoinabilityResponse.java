package me.vrekt.fortnitexmpp.implementation.party.packet.authentication.server;

import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

/**
 * A packet used for responding to a query joinability request.
 */
public final class PacketPartyQueryJoinabilityResponse implements PartyPacket {

    private final String payload;
    private Jid to;

    /**
     * Initialize the packet
     *
     * @param partyId       the ID of the party
     * @param allowedToJoin if the client is allowed to join
     * @param to            who it is sent to
     */
    public PacketPartyQueryJoinabilityResponse(String partyId, boolean allowedToJoin, Jid to) {
        final var type = PartyPacketType.PARTY_QUERY_JOINABILITY_RESPONSE;
        this.to = to;

        var payload = Json.createObjectBuilder();
        payload.add("partyId", partyId);
        payload.add("isJoinable", allowedToJoin);
        payload.add("rejectionType", 0); // TODO: Find other rejection types?
        payload.add("resultParam", "");
        this.payload = PacketBuilder.buildPacket(payload.build(), type).toString();
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
