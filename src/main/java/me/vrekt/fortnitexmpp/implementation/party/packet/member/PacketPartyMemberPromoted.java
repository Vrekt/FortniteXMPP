package me.vrekt.fortnitexmpp.implementation.party.packet.member;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

/**
 * A packet used for promoting party members.
 * This can be used to promote yourself, or promote another member.
 */
public final class PacketPartyMemberPromoted implements PartyPacket {

    private final String payload;
    private final Jid to;

    public PacketPartyMemberPromoted(Party party, String memberToPromoteId, boolean wasFromLeaderLeaving) {
        final var type = PartyPacketType.PARTY_MEMBER_PROMOTED;
        this.to = party.getFrom();
        this.payload = PacketBuilder.buildPacket(Json.createObjectBuilder()
                .add("partyId", party.getId())
                .add("promotedMemberUserId", memberToPromoteId)
                .add("fromLeaderLeaving", wasFromLeaderLeaving).build(), type).toString();
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
