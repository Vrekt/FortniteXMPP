package me.vrekt.fortnitexmpp.implementation.party.packet.general;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

/**
 * A packet used for sending invites to other players
 * Some magic values that should be changed in the future ??
 * TODO: "partyTypeId" = 286331153 (as of 2/22/19)
 */
public final class PacketPartyInvitation implements PartyPacket {

    private final String payload;
    private final Jid to;

    /**
     * Initialize the packet
     *
     * @param partyId     the ID of the party
     * @param key         the key of the party
     * @param displayName the display name of YOUR account, not whoever you are sending the invite to.
     * @param to          who the invite is to.
     */
    public PacketPartyInvitation(String partyId, String key, String displayName, Jid to) {
        final var type = PartyPacketType.PARTY_INVITATION;
        this.to = to;
        var payload = Json.createObjectBuilder();

        payload.add("partyId", partyId);
        payload.add("partyTypeId", 286331153);
        payload.add("displayName", displayName);
        payload.add("accessKey", key);
        payload.add("appId", "Fortnite");
        payload.add("buildId", Party.DEFAULT_BUILD_ID);
        this.payload = PacketBuilder.buildPacket(payload.build(), type).toString();
    }

    /**
     * Initialize the packet
     *
     * @param party       the party
     * @param displayName the display name of YOUR account, not whoever you are sending the invite to.
     * @param to          who the invite is to.
     */
    public PacketPartyInvitation(Party party, String displayName, Jid to) {
        this(party.getId(), party.getAccessKey(), displayName, to);
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
