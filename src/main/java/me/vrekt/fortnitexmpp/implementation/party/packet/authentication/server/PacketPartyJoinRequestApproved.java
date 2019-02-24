package me.vrekt.fortnitexmpp.implementation.party.packet.authentication.server;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.member.PartyMember;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;
import java.util.Set;

/**
 * A packet used for approving a party join request
 */
public final class PacketPartyJoinRequestApproved implements PartyPacket {

    private final String payload;
    private final Jid to;

    /**
     * Initialize the packet
     *
     * @param party the party, this method uses the party configuration for building the packet,
     *              so make sure the configuration is correct before building.
     * @param to    who the packet is to
     */
    public PacketPartyJoinRequestApproved(Party party, Jid to) {
        final var type = PartyPacketType.PARTY_JOIN_REQUEST_APPROVED;
        this.to = to;
        var payload = Json.createObjectBuilder();

        payload.add("partyId", party.getId());
        payload.add("presencePermissions", party.getConfiguration().getPresencePermissions());
        payload.add("invitePermissions", party.getConfiguration().getPermissions().getInvitePermissions());
        payload.add("partyFlags", party.getConfiguration().getPermissions().getPartyFlags());
        payload.add("notAcceptingMembersReason", party.getConfiguration().getPermissions().getNotAcceptingMembersReason());
        payload.add("maxMembers", party.getConfiguration().getMaxMembers());
        payload.add("password", "");
        payload.add("accessKey", party.getAccessKey());

        var members = Json.createArrayBuilder();
        party.getMembers().forEach(m -> {
            final var member = Json.createObjectBuilder();
            member.add("userId", m.getAccountId());
            member.add("xmppResource", m.getResource());
            member.add("displayName", m.getDisplayName());
            members.add(member);
        });

        payload.add("members", members.build());
        this.payload = PacketBuilder.buildPacket(payload.build(), type).toString();
    }

    /**
     * Initialize the packet
     *
     * @param partyId             the ID of the party
     * @param key                 the key of the party
     * @param memberList          the member list, usually the already created party object would have this or you stored it yourself.
     * @param presencePermissions the presence permissions, this seems to be a random value each time, but I always make it "-1692261632"
     * @param maxMembers          the max members allowed for this party
     * @param to                  who the packet is to
     */
    public PacketPartyJoinRequestApproved(String partyId, String key, Set<PartyMember> memberList, long presencePermissions, int maxMembers, Jid to) {
        final var type = PartyPacketType.PARTY_JOIN_REQUEST_APPROVED;
        this.to = to;
        var payload = Json.createObjectBuilder();

        payload.add("partyId", partyId);
        payload.add("presencePermissions", presencePermissions);
        payload.add("invitePermissions", 3);
        payload.add("partyFlags", 3);
        payload.add("notAcceptingMembersReason", 0);
        payload.add("maxMembers", maxMembers);
        payload.add("password", "");
        payload.add("accessKey", key);

        var members = Json.createArrayBuilder();

        memberList.forEach(m -> {
            var member = Json.createObjectBuilder();
            member.add("userId", m.getAccountId());
            member.add("xmppResource", m.getResource());
            member.add("displayName", m.getDisplayName());
            members.add(member);
        });

        payload.add("members", members.build());
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
