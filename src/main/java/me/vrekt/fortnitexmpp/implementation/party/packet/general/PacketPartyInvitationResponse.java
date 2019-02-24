package me.vrekt.fortnitexmpp.implementation.party.packet.general;

import me.vrekt.fortnitexmpp.implementation.party.event.invite.PartyInvitationResponseEvent;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

/**
 * A packet used for responding to an invite.
 * 1 = Accepted
 * 2 = Rejected
 * NOTE: This is a bit weird, sometimes invitation responses aren't sent so do not rely on this!
 * I've only found consistent results with somebody rejecting an invite.
 */
public final class PacketPartyInvitationResponse implements PartyPacket {

    private final String payload;
    private final Jid to;

    public PacketPartyInvitationResponse(String partyId, PartyInvitationResponseEvent.Response response, Jid to) {
        final var type = PartyPacketType.PARTY_INVITATION_RESPONSE;
        this.to = to;
        this.payload = PacketBuilder.buildPacket(Json.createObjectBuilder().add("partyId", partyId).add("response", response.getCode()).build(), type).toString();
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
