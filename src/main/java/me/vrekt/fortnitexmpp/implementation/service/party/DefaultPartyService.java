package me.vrekt.fortnitexmpp.implementation.service.party;

import me.vrekt.fortnitexmpp.FortniteXmpp;
import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.PartyListener;
import me.vrekt.fortnitexmpp.implementation.party.event.data.PartyConfigurationUpdatedEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.data.PartyDataEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.invite.PartyInvitationEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.invite.PartyInvitationResponseEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.join.PartyJoinRequestApprovedEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.join.PartyJoinRequestEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.join.PartyQueryJoinabilityEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.member.PartyMemberExitedEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.member.PartyMemberJoinedEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.member.PartyMemberPromotedEvent;
import me.vrekt.fortnitexmpp.implementation.party.member.PartyMember;
import me.vrekt.fortnitexmpp.implementation.party.member.data.MemberDataBuildType;
import me.vrekt.fortnitexmpp.implementation.party.member.data.PartyMemberData;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.Jid;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultPartyService implements PartyService {

    private final Set<PartyListener> listeners = ConcurrentHashMap.newKeySet();
    private final XMPPTCPConnection connection;
    private final FortniteXmpp fortniteXmpp;
    private final PacketListener packetListener = new PacketListener();

    private Party party;

    public DefaultPartyService(FortniteXmpp fortniteXmpp) {
        this.connection = fortniteXmpp.getConnection();
        this.fortniteXmpp = fortniteXmpp;
        connection.addAsyncStanzaListener(packetListener, new StanzaTypeFilter(Message.class));
    }

    @Override
    public void addPartyListener(PartyListener listener) {
        listeners.add(listener);

    }

    @Override
    public void removePartyListener(PartyListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void sendPacket(PartyPacket partyPacket) {
        try {
            connection.sendStanza(new Message(partyPacket.getTo(), partyPacket.getPayload()));
        } catch (SmackException.NotConnectedException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendPacketTo(PartyPacket partyPacket, Jid to) {
        try {
            connection.sendStanza(new Message(to, partyPacket.getPayload()));
        } catch (SmackException.NotConnectedException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendPacketToAll(PartyPacket partyPacket, Collection<PartyMember> members) {
        try {
            for (PartyMember member : members) {
                if (member.getJid().equals(fortniteXmpp.getJid())) continue;
                connection.sendStanza(new Message(member.getJid(), partyPacket.getPayload()));
            }
        } catch (SmackException.NotConnectedException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Optional<Party> getPartyContext() {
        return Optional.ofNullable(party);
    }

    @Override
    public void close() {
        connection.removeAsyncStanzaListener(packetListener);
        listeners.clear();
    }

    /**
     * Updates the party
     *
     * @param newParty the new party
     * @return the updated party
     */
    private Party updateParty(Party newParty) {
        if (this.party == null) {
            this.party = newParty;
            return newParty;
        }

        if (!newParty.getId().equals(this.party.getId())) {
            this.party = newParty;
        }
        return party;
    }

    private class PacketListener implements StanzaListener {
        @Override
        public void processStanza(Stanza packet) {
            if (!(packet instanceof Message)) return;
            var message = (Message) packet;
            if (message.getType() != Message.Type.normal) return;

            listeners.forEach(listener -> listener.onMessageReceived(message));

            try {
                    var reader = Json.createReader(new StringReader(message.getBody()));
                    var data = reader.readObject();
                    reader.close();

                var payload = data.getJsonObject("payload");
                var type = data.getString("type");
                var packetType = PartyPacketType.getFrom(type);
                if (packetType == PartyPacketType.PARTY_UNKNOWN_TYPE) return;

                var party = Party.newBuilder(payload, message.getFrom()).build();
                var updated = updateParty(party);

                postPartyEvent(packetType, updated, payload, message.getFrom());
            } catch (Exception exception) {
                System.err.println("Encountered an exception while parsing message json!");
                exception.printStackTrace();
            }
        }
    }

    private void postPartyEvent(PartyPacketType packetType, Party party, JsonObject payload, Jid from) {
        switch (packetType) {
            case PARTY_INVITATION:
                final var invitationEvent = new PartyInvitationEvent(party, this::acceptPartyInvitation, from);
                listeners.forEach(listener -> listener.onInvitation(invitationEvent));
                break;
            case PARTY_INVITATION_RESPONSE:
                final var invitationResponseEvent = new PartyInvitationResponseEvent(party, payload, from);
                listeners.forEach(listener -> listener.onInvitationResponse(invitationResponseEvent));
                break;
            case PARTY_JOIN_REQUEST:
                final var partyJoinRequestEvent = new PartyJoinRequestEvent(party, payload, from);
                listeners.forEach(listener -> listener.onJoinRequest(partyJoinRequestEvent));
                break;
            case PARTY_JOIN_REQUEST_APPROVED:
                final var partyJoinRequestApprovedEvent = new PartyJoinRequestApprovedEvent(party, payload, from);
                listeners.forEach(listener -> listener.onJoinRequestApproved(partyJoinRequestApprovedEvent));
                break;
            case PARTY_JOIN_REQUEST_REJECTED:
                listeners.forEach(listener -> listener.onJoinRequestRejected(party, from));
                break;
            case PARTY_JOIN_ACKNOWLEDGED:
                listeners.forEach(listener -> listener.onJoinAcknowledged(party, from));
                break;
            case PARTY_JOIN_ACKNOWLEDGED_RESPONSE:
                listeners.forEach(listener -> listener.onJoinAcknowledgedResponse(party, from));
                break;
            case PARTY_QUERY_JOINABILITY:
                final var partyQueryJoinabilityEvent = new PartyQueryJoinabilityEvent(party, payload, from);
                listeners.forEach(listener -> listener.onPartyQueryJoinability(partyQueryJoinabilityEvent));
                break;
            case PARTY_QUERY_JOINABILITY_RESPONSE:
                listeners.forEach(listener -> listener.onPartyQueryJoinabilityResponse(party, from));
                break;
            case PARTY_MEMBER_DATA:
                listeners.forEach(listener -> listener.onPartyMemberData(party, PartyMemberData.newBuilder(MemberDataBuildType.OTHER_MEMBER).setPayload(payload).build(), from));
                break;
            case PARTY_MEMBER_PROMOTED:
                final var partyMemberPromotedEvent = new PartyMemberPromotedEvent(party, payload, from);
                listeners.forEach(listener -> listener.onPartyMemberPromoted(partyMemberPromotedEvent));
                break;
            case PARTY_MEMBER_EXITED:
                final var partyMemberExitedEvent = new PartyMemberExitedEvent(party, payload, from);
                listeners.forEach(listener -> listener.onPartyMemberExited(partyMemberExitedEvent));
                break;
            case PARTY_MEMBER_JOINED:
                final var partyMemberJoinedEvent = new PartyMemberJoinedEvent(party, payload, from);
                listeners.forEach(listener -> listener.onPartyMemberJoined(partyMemberJoinedEvent));
                break;
            case PARTY_CONFIGURATION:
                final var partyConfigurationEvent = new PartyConfigurationUpdatedEvent(party, payload, from);
                listeners.forEach(listener -> listener.onPartyConfigurationUpdated(partyConfigurationEvent));
                break;
            case PARTY_DATA:
                final var partyDataEvent = new PartyDataEvent(party, payload, from);
                listeners.forEach(listener -> listener.onPartyDataReceived(partyDataEvent));
                break;
        }
    }

    private void acceptPartyInvitation(PartyJoinData data) {
        data.accept(this, fortniteXmpp);
    }
}
