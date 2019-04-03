package me.vrekt.fortnitexmpp.party;

import com.google.common.flogger.FluentLogger;
import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.party.implementation.DefaultParty;
import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.configuration.PartyConfiguration;
import me.vrekt.fortnitexmpp.party.implementation.configuration.PrivacySetting;
import me.vrekt.fortnitexmpp.party.implementation.data.ImmutablePartyData;
import me.vrekt.fortnitexmpp.party.implementation.listener.PartyListener;
import me.vrekt.fortnitexmpp.party.implementation.member.PartyMember;
import me.vrekt.fortnitexmpp.party.implementation.member.data.ImmutablePartyMemberData;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.general.InvitationResponse;
import me.vrekt.fortnitexmpp.party.type.PartyType;
import me.vrekt.fortnitexmpp.utility.FindPlatformUtility;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public final class DefaultPartyResource implements PartyResource {

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private final Map<String, Party> parties = new ConcurrentHashMap<>();
    private final List<PartyListener> listeners = new CopyOnWriteArrayList<>();
    private final MessageListener messageListener = new MessageListener();
    private final XMPPTCPConnection connection;

    private boolean log;

    /**
     * Initialize this resource
     *
     * @param fortniteXMPP the {@link FortniteXMPP} instance
     */
    public DefaultPartyResource(final FortniteXMPP fortniteXMPP) {
        this.connection = fortniteXMPP.connection();
        connection.addAsyncStanzaListener(messageListener, StanzaTypeFilter.MESSAGE);
    }

    @Override
    public void addPartyListener(final PartyListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removePartyListener(final PartyListener listener) {
        listeners.add(listener);
    }

    @Override
    public void sendRequestTo(final PartyRequest request, final Jid recipient) {
        sendTo(request, recipient);
    }

    @Override
    public void sendRequestTo(final PartyRequest request, final Collection<Jid> recipients) {
        recipients.forEach(jid -> sendTo(request, jid));
    }

    @Override
    public void sendRequestTo(final PartyRequest request, final Iterable<PartyMember> members) {
        members.forEach(member -> sendTo(request, member.user()));
    }

    @Override
    public boolean trySendRequestTo(PartyRequest request, Jid recipient) {
        return sendTo(request, recipient);
    }

    @Override
    public boolean trySendRequestTo(PartyRequest request, Collection<Jid> recipients) {
        final var failed = new AtomicBoolean(false);
        recipients.forEach(recipient -> {
            if (sendTo(request, recipient)) failed.set(true);
        });
        return failed.get();
    }

    @Override
    public boolean trySendRequestTo(PartyRequest request, Iterable<PartyMember> members) {
        final var failed = new AtomicBoolean(false);
        members.forEach(recipient -> {
            if (sendTo(request, recipient.user())) failed.set(true);
        });
        return failed.get();
    }

    /**
     * Send a request to the specified recipient
     *
     * @param request   the request
     * @param recipient the recipient
     */
    private boolean sendTo(final PartyRequest request, final Jid recipient) {
        try {
            final var message = new Message(recipient, Message.Type.normal);
            message.setBody(request.payload());
            connection.sendStanza(message);
        } catch (final SmackException.NotConnectedException | InterruptedException exception) {
            // TODO: Ignore log variable here. Maybe change later.
            LOGGER.atSevere().log("Could not send party request!");
            return true;
        }
        return false;
    }

    @Override
    public Party getPartyById(String partyId) {
        return parties.get(partyId);
    }

    @Override
    public void removePartyById(String partyId) {
        parties.remove(partyId);
    }

    @Override
    public void close() {
        connection.removeAsyncStanzaListener(messageListener);
        listeners.clear();
        parties.clear();
    }

    /**
     * @param log {@code true} if this resource should log exceptions and warnings.
     */
    public void logExceptionsAndWarnings(final boolean log) {
        this.log = log;
    }

    /**
     * Listens for the party messages
     */
    private final class MessageListener implements StanzaListener {
        @Override
        public void processStanza(final Stanza packet) {
            final var message = (Message) packet;
            if (message.getType() != Message.Type.normal) return;

            try {
                final var reader = Json.createReader(new StringReader(message.getBody()));
                final var data = reader.readObject();
                reader.close();

                // TODO: Move this down later? This will print messages from other stuff like friends, etc.
                listeners.forEach(listener -> listener.onMessageReceived(message));

                final var payload = data.getJsonObject("payload");

                final var type = PartyType.typeOf(data.getString("type"));
                if (type == null) return; // not relevant

                // update the build id
                if (payload.containsKey("buildId")) {
                    DefaultParty.buildId = Integer.valueOf(payload.getString("buildId"));
                } else if (payload.containsKey("buildid")) {
                    DefaultParty.buildId = Integer.valueOf(payload.getString("buildid"));
                }

                // return here since we received something from ourself.
                if (message.getFrom().asEntityFullJidIfPossible().equals(connection.getUser())) return;

                final var partyId = JsonUtility.getString("partyId", payload);
                if (partyId.isEmpty()) return; // invalid packet?
                final var from = message.getFrom();

                var party = parties.get(partyId.get());
                if (party == null) {
                    party = Party.fromPayload(payload);
                    parties.put(partyId.get(), party);
                }

                // update the party and then invoke listeners.
                updatePartyBasedOnType(party, type, payload, from);
                invokeListeners(party, type, payload, from);
            } catch (final Exception exception) {
                if (log) LOGGER.atWarning().log("Failed to parse message party JSON.");
            }
        }
    }

    /**
     * Updates the party based on what type of packet was received.
     *
     * @param party   the party
     * @param type    the type of packet
     * @param payload the payload sent
     * @param from    who it was sent from
     */
    private void updatePartyBasedOnType(final Party party, final PartyType type, final JsonObject payload, final Jid from) {

        // join request has data about party members already in the party.
        if (type == PartyType.PARTY_JOIN_REQUEST_APPROVED) {
            JsonUtility.getArray("members", payload).ifPresentOrElse(array -> array.forEach(value -> {
                final var object = value.asJsonObject();
                party.addMember(PartyMember.newMember(object));
            }), () -> logMalformedType(type, payload, from));
            // a member joined, verify the request is valid.
        } else if (type == PartyType.PARTY_MEMBER_JOINED) {
            JsonUtility.getObject("member", payload)
                    .ifPresentOrElse(object -> party.addMember(PartyMember.newMember(object)), () -> logMalformedType(type, payload, from));
            // a member exited
        } else if (type == PartyType.PARTY_MEMBER_EXITED) {
            final var memberId = JsonUtility.getString("memberId", payload);
            final var kicked = JsonUtility.getBoolean("wasKicked", payload);
            if (memberId.isEmpty() || kicked.isEmpty()) {
                logMalformedType(type, payload, from);
                return;
            }

            party.removeMemberById(memberId.get());
            // party data was received
        } else if (type == PartyType.PARTY_DATA) {
            final var innerPayload = JsonUtility.getObject("payload", payload);
            final var attributes = JsonUtility.getObject("Attrs", innerPayload.orElse(null));
            final var privacySettings_j = JsonUtility.getObject("PrivacySettings_j", attributes.orElse(null));
            final var privacySettings = JsonUtility.getObject("PrivacySettings", privacySettings_j.orElse(null));

            // TODO: Implement new squad assignment data, just return for now since there is no useful data here.
            if (innerPayload.isEmpty() || attributes.isEmpty() || privacySettings_j.isEmpty() || privacySettings.isEmpty()) return;

            // reverse this, because the original value is called "onlyLeaderFriendsCanJoin"
            final var allowFriendsOfFriends = !JsonUtility.getBoolean("bOnlyLeaderFriendsCanJoin", privacySettings.get()).orElse(false);
            final var partyType = JsonUtility.getString("partType", privacySettings.get()).orElse("Public");

            // public party
            if (partyType.equalsIgnoreCase("Public")) {
                party.updateConfiguration(new PartyConfiguration(PrivacySetting.PUBLIC, party.configuration().maxMembers(), party.configuration().presencePermissions()));
                // private party
            } else if (partyType.equalsIgnoreCase("Private")) {
                party.updateConfiguration(new PartyConfiguration(allowFriendsOfFriends ? PrivacySetting.PRIVATE_ALLOW_FRIENDS_OF_FRIENDS : PrivacySetting.PRIVATE,
                        party.configuration().maxMembers(), party.configuration().presencePermissions()));
                // friends only
            } else if (partyType.equalsIgnoreCase("FriendsOnly")) {
                party.updateConfiguration(new PartyConfiguration(allowFriendsOfFriends ? PrivacySetting.FRIENDS_ALLOW_FRIENDS_OF_FRIENDS : PrivacySetting.FRIENDS,
                        party.configuration().maxMembers(), party.configuration().presencePermissions()));
            }
            // the configuration
        } else if (type == PartyType.PARTY_CONFIGURATION) {
            final var presencePermissions = JsonUtility.getLong("presencePermissions", payload);
            final var invitePermissions = JsonUtility.getInt("invitePermissions", payload);
            final var partyFlags = JsonUtility.getInt("partyFlags", payload);
            final var notAcceptingMembersReason = JsonUtility.getInt("notAcceptingMembersReason", payload);
            final var maxMembers = JsonUtility.getInt("maxMembers", payload);
            if (presencePermissions.isEmpty() || invitePermissions.isEmpty() || partyFlags.isEmpty() || notAcceptingMembersReason.isEmpty() || maxMembers.isEmpty()) {
                logMalformedType(type, payload, from);
                return;
            }

            party.updateConfiguration(new PartyConfiguration(invitePermissions.get(), partyFlags.get(), notAcceptingMembersReason.get(), maxMembers.get(), presencePermissions.get()));
        }
    }

    /**
     * Invokes the listeners for what type was received and parses the payload if needed.
     * This method logs a warning and returns if an invalid payload was received.
     *
     * @param party the party
     * @param type  the type of packet
     * @param from  who it was sent from
     */
    private void invokeListeners(final Party party, final PartyType type, final JsonObject payload, final Jid from) {
        if (type == PartyType.PARTY_INVITATION) {
            listeners.forEach(listener -> listener.onInvitation(party, from));
            // invitation response, not always sent. Really only sent with rejected responses.
        } else if (type == PartyType.PARTY_INVITATION_RESPONSE) {
            final var response = JsonUtility.getInt("response", payload);
            if (response.isEmpty()) {
                logMalformedType(type, payload, from);
                return;
            }

            listeners.forEach(listener -> listener.onInvitationResponse(party, response.get() == 1 ? InvitationResponse.ACCEPTED : InvitationResponse.REJECTED, from));
            // query if the party is joinable.
        } else if (type == PartyType.PARTY_QUERY_JOINABILITY) {
            final var joinData = JsonUtility.getObject("joinData", payload);
            final var attributes = JsonUtility.getObject("Attrs", joinData.orElse(null));
            final var crossplayPreference = JsonUtility.getInt("CrossplayPreference_i", attributes.orElse(null));
            if (joinData.isEmpty() || attributes.isEmpty() || crossplayPreference.isEmpty()) {
                logMalformedType(type, payload, from);
                return;
            }
            listeners.forEach(listener -> listener.onQueryJoinability(party, crossplayPreference.get(), from));
            // the response if the party is joinable.
        } else if (type == PartyType.PARTY_QUERY_JOINABILITY_RESPONSE) {
            final var isJoinable = JsonUtility.getBoolean("isJoinable", payload);
            final var rejectionType = JsonUtility.getInt("rejectionType", payload);
            final var resultParam = JsonUtility.getString("resultParam", payload);
            if (isJoinable.isEmpty() || rejectionType.isEmpty() || resultParam.isEmpty()) {
                logMalformedType(type, payload, from);
                return;
            }
            listeners.forEach(listener -> listener.onQueryJoinabilityResponse(party, isJoinable.get(), rejectionType.get(), resultParam.get(), from));
            // join request
        } else if (type == PartyType.PARTY_JOIN_REQUEST) {
            final var accountId = from.getLocalpartOrNull();
            final var resource = from.getResourceOrNull();
            if (accountId == null || resource == null) {
                logMalformedType(type, payload, from);
                return;
            }

            // YIKES formatting
            JsonUtility.getString("displayName", payload)
                    .ifPresent(displayName -> listeners
                            .forEach(listener -> listener.onJoinRequest(party, PartyMember
                                    .newMember(accountId.asUnescapedString(), resource.toString(), displayName,
                                            FindPlatformUtility.getPlatformForResource(resource.toString())), from)));
            // request rejected
        } else if (type == PartyType.PARTY_JOIN_REQUEST_REJECTED) {
            listeners.forEach(listener -> listener.onJoinRequestRejected(party, from));
            // join request approved
        } else if (type == PartyType.PARTY_JOIN_REQUEST_APPROVED) {
            final var members = JsonUtility.getArray("members", payload);
            if (members.isEmpty()) {
                logMalformedType(type, payload, from);
                return;
            }
            final var array = members.get();
            final var set = new HashSet<PartyMember>();
            array.forEach(value -> set.add(PartyMember.newMember(value.asJsonObject())));
            set.forEach(party::addMember);

            listeners.forEach(listener -> listener.onJoinRequestApproved(party, set, from));
            // a join was acknowledged
        } else if (type == PartyType.PARTY_JOIN_ACKNOWLEDGED) {
            listeners.forEach(listener -> listener.onJoinAcknowledged(party, from));
            // a response to the join acknowledged
        } else if (type == PartyType.PARTY_JOIN_ACKNOWLEDGED_RESPONSE) {
            listeners.forEach(listener -> listener.onJoinAcknowledgedResponse(party, from));
        } else if (type == PartyType.PARTY_MEMBER_DATA) {
            listeners.forEach(listener -> listener.onPartyMemberDataReceived(party, ImmutablePartyMemberData.adaptFrom(payload), from));
            // a member joined.
        } else if (type == PartyType.PARTY_MEMBER_JOINED) {
            listeners.forEach(listener -> listener.onPartyMemberJoined(party, PartyMember.newMember(payload), from));
            // a member exited.
        } else if (type == PartyType.PARTY_MEMBER_EXITED) {
            final var accountId = JsonUtility.getString("memberId", payload);
            final var wasKicked = JsonUtility.getBoolean("wasKicked", payload);
            if (accountId.isEmpty() || wasKicked.isEmpty()) {
                logMalformedType(type, payload, from);
                return;
            }
            listeners.forEach(listener -> listener.onPartyMemberExited(party, accountId.get(), wasKicked.get(), from));
            // a member was promoted
        } else if (type == PartyType.PARTY_MEMBER_PROMOTED) {
            final var accountId = JsonUtility.getString("promotedMemberUserId", payload);
            final var wasFromLeaderLeaving = JsonUtility.getBoolean("fromLeaderLeaving", payload);
            if (accountId.isEmpty() || wasFromLeaderLeaving.isEmpty()) {
                logMalformedType(type, payload, from);
                return;
            }
            listeners.forEach(listener -> listener.onPartyMemberPromoted(party, accountId.get(), wasFromLeaderLeaving.get(), from));
            // party config, privacy related.
        } else if (type == PartyType.PARTY_CONFIGURATION) {
            final var presencePermissions = JsonUtility.getLong("presencePermissions", payload);
            final var invitePermissions = JsonUtility.getInt("invitePermissions", payload);
            final var partyFlags = JsonUtility.getInt("partyFlags", payload);
            final var notAcceptingMembersReason = JsonUtility.getInt("notAcceptingMembersReason", payload);
            final var maxMembers = JsonUtility.getInt("maxMembers", payload);
            if (presencePermissions.isEmpty() || invitePermissions.isEmpty() || partyFlags.isEmpty() || notAcceptingMembersReason.isEmpty() || maxMembers.isEmpty()) {
                logMalformedType(type, payload, from);
                return;
            }
            listeners.forEach(listener -> listener.onPartyConfigurationUpdated(party,
                    new PartyConfiguration(invitePermissions.get(), partyFlags.get(), notAcceptingMembersReason.get(), maxMembers.get(), presencePermissions.get()), from));
            // party data
        } else if (type == PartyType.PARTY_DATA) {
            final var data = ImmutablePartyData.adaptFrom(payload);
            if (data == null) {
                logMalformedType(type, payload, from);
                return;
            }
            listeners.forEach(listener -> listener.onPartyData(party, data, from));
        }
    }

    /**
     * Log the malformed type received
     *
     * @param type    the type
     * @param payload the payload sent
     * @param from    who it was sent from
     */
    private void logMalformedType(final PartyType type, final JsonObject payload, final Jid from) {
        if (log)
            LOGGER.atWarning().log("Invalid party message received from: " + from.asUnescapedString() + "\nType: " + type.getName() + "\nPayload as string: " + payload.toString());
    }

}
