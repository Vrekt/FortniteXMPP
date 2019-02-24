package me.vrekt.fortnitexmpp.implementation.party.packet.member;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

/**
 * A packet used for notifying of a party member leaving.
 * When a normal player leaves they will send this. Its up to the bot to broadcast it to other clients.
 */
public final class PacketPartyMemberExited implements PartyPacket {

    private final String payload;
    private final Jid to;

    public PacketPartyMemberExited(Party party, String accountId, boolean wasKicked) {
        final var type = PartyPacketType.PARTY_MEMBER_EXITED;
        this.to = party.getFrom();
        this.payload = PacketBuilder.buildPacket(Json.createObjectBuilder()
                .add("partyId", party.getId())
                .add("memberId", accountId)
                .add("wasKicked", wasKicked).build(), type).toString();
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
