package me.vrekt.fortnitexmpp.implementation.party.packet.member;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

/**
 * A packet used for notifying other clients of a party member who joined
 * If the bot is hosting a party, the clients join do NOT send this packet, its up to the
 * "server" (in this case the bot) to send the packet
 */
public final class PacketPartyMemberJoined implements PartyPacket {

    private final String payload;
    private final Jid to;

    public PacketPartyMemberJoined(Party party, String accountId, String xmppResource, String displayName) {
        final var type = PartyPacketType.PARTY_MEMBER_JOINED;
        this.to = party.getFrom();

        var payload = Json.createObjectBuilder();
        var member = Json.createObjectBuilder();

        payload.add("partyId", party.getId());
        member.add("userId", accountId);
        member.add("xmppResource", xmppResource);
        member.add("displayName", displayName);
        payload.add("member", member.build());
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
