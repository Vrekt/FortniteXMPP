package me.vrekt.fortnitexmpp.implementation.party.packet.authentication.client;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

public final class PacketPartyQueryJoinability implements PartyPacket {

    private final String payload;
    private final Jid to;

    /**
     * Initialize the packet
     *
     * @param party               the party
     * @param crossplayPreference the crossplay preference of the account.
     *                            Rumor:
     *                            1 == Opted in
     *                            ?? unknown rest.
     * @param to                  who it is going to
     */
    public PacketPartyQueryJoinability(Party party, int crossplayPreference, Jid to) {
        this(party.getId(), party.getAccessKey(), crossplayPreference, to);
    }

    /**
     * Initialize the packet
     *
     * @param partyId             the ID of the party
     * @param accessKey           the access key of the party
     * @param crossplayPreference the crossplay preference of the account.
     *                            Rumor:
     *                            1 == Opted in
     *                            ?? unknown rest.
     * @param to                  who it is going to
     */
    public PacketPartyQueryJoinability(String partyId, String accessKey, int crossplayPreference, Jid to) {
        final var type = PartyPacketType.PARTY_QUERY_JOINABILITY;
        this.to = to;
        var payload = Json.createObjectBuilder();

        payload.add("partyId", partyId);
        payload.add("accessKey", accessKey);
        payload.add("appid", "Fortnite");
        payload.add("buildid", Party.DEFAULT_BUILD_ID);
        payload.add("joinData", Json.createObjectBuilder()
                .add("Rev", PacketBuilder.getRevForType(type))
                .add("Attrs", Json.createObjectBuilder()
                        .add("CrossplayPreference_i", crossplayPreference)
                        .build()).build());
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
