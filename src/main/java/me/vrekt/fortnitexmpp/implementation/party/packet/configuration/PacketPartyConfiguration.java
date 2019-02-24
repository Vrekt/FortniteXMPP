package me.vrekt.fortnitexmpp.implementation.party.packet.configuration;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

/**
 * A packet used for sending a party configuration
 */
public final class PacketPartyConfiguration implements PartyPacket {

    private final String payload;
    private final Jid to;

    public PacketPartyConfiguration(Party party, PartyConfiguration configuration) {
        final var type = PartyPacketType.PARTY_CONFIGURATION;
        this.to = party.getFrom();

        var permissions = configuration.getPermissions();
        var payload = Json.createObjectBuilder();
        payload.add("partyId", party.getId());
        payload.add("presencePermissions", configuration.getPresencePermissions());
        payload.add("invitePermissions", permissions.getInvitePermissions());
        payload.add("partyFlags", permissions.getPartyFlags());
        payload.add("notAcceptingMembersReason", permissions.getNotAcceptingMembersReason());
        payload.add("maxMembers", configuration.getMaxMembers());
        payload.add("password", "");
        payload.add("accessKey", party.getAccessKey());
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
