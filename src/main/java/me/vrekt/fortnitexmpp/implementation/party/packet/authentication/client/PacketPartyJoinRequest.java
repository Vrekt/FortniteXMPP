package me.vrekt.fortnitexmpp.implementation.party.packet.authentication.client;

import me.vrekt.fortnitexmpp.implementation.PlatformType;
import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

/**
 * A packet used for requesting to join a party.
 */
public final class PacketPartyJoinRequest implements PartyPacket {

    private final Jid to;
    private final String payload;

    /**
     * Initialize the packet
     *
     * @param party       the party
     * @param displayName the display name of the account attempting to join a party (in most cases your account)
     *                    for example, I "ABC" am trying to join "123"'s party, my displayName is "ABC"
     * @param platform    the platform of your account
     */
    public PacketPartyJoinRequest(Party party, String displayName, PlatformType platform) {
        final var type = PartyPacketType.PARTY_JOIN_REQUEST;
        this.to = party.getFrom();

        var payload = Json.createObjectBuilder();
        payload.add("partyId", party.getId());
        payload.add("accessKey", party.getAccessKey());
        payload.add("displayName", displayName);
        payload.add("platform", platform.name());
        payload.add("appId", "Fortnite");
        payload.add("buildId", Party.DEFAULT_BUILD_ID);
        this.payload = PacketBuilder.buildPacket(payload.build(), type).toString();
    }

    public PacketPartyJoinRequest(String partyId, String accessKey, String displayName, PlatformType platform, Jid to) {
        final var type = PartyPacketType.PARTY_JOIN_REQUEST;
        this.to = to;

        var payload = Json.createObjectBuilder();
        payload.add("partyId", partyId);
        payload.add("accessKey", accessKey);
        payload.add("displayName", displayName);
        payload.add("platform", platform.name());
        payload.add("appId", "Fortnite");
        payload.add("buildId", Party.DEFAULT_BUILD_ID);
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
