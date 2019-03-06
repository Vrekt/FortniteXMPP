package me.vrekt.fortnitexmpp.implementation.party;

import me.vrekt.fortnitexmpp.implementation.party.member.PartyMember;
import me.vrekt.fortnitexmpp.implementation.party.packet.configuration.PacketPartyConfiguration;
import me.vrekt.fortnitexmpp.implementation.party.packet.configuration.PartyConfiguration;
import me.vrekt.fortnitexmpp.implementation.party.packet.configuration.privacy.PrivacyType;
import me.vrekt.fortnitexmpp.implementation.party.packet.data.PacketPartyData;
import me.vrekt.fortnitexmpp.implementation.party.packet.data.PartyData;
import me.vrekt.fortnitexmpp.implementation.party.packet.data.PartyDataType;
import me.vrekt.fortnitexmpp.implementation.service.party.PartyService;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.apache.commons.lang3.RandomStringUtils;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Party {

    public static final String DEFAULT_BUILD_ID = "5209636";

    private final String id, accessKey;
    private final Jid from;

    private final Set<PartyMember> members = new HashSet<>();
    private PartyConfiguration configuration;

    private Party(Builder builder) {
        this.id = builder.id;
        this.accessKey = builder.accessKey;
        this.from = builder.from;

        configuration = new PartyConfiguration(builder.invitePermissions, builder.partyFlags, builder.notAcceptingMembersReason, builder.maxMembers, builder.presencePermissions);
    }

    public String getId() {
        return id;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public Jid getFrom() {
        return from;
    }

    public Set<PartyMember> getMembers() {
        return members;
    }

    public void addMember(PartyMember member) {
        members.add(member);
    }

    public void removeMember(PartyMember member) {
        members.remove(member);
    }

    public void removeMemberById(String accountId) {
        members.removeIf(member -> member.getAccountId().equals(accountId));
    }

    /**
     * Updates the party configuration
     *
     * @param type                  the type of privacy
     * @param allowFriendsOfFriends whether or not to allow friends of friends
     * @param maxMembers            how many members are allowed
     */
    public void updateConfiguration(PrivacyType type, boolean allowFriendsOfFriends, int maxMembers, long presencePermissions) {
        configuration = new PartyConfiguration(type, allowFriendsOfFriends, maxMembers, presencePermissions);
    }

    /**
     * Update the configuration
     *
     * @param configuration the configuration
     */
    public void updateConfiguration(PartyConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Used to send the updated privacy settings
     *
     * @param service the party service
     * @param to      the JID who it is sent to.
     */
    public void sendPrivacyConfigurationTo(PartyService service, Jid to) {
        if (configuration == null) return;

        final var configurationPacket = new PacketPartyConfiguration(this, configuration);
        final var dataBuilder = PartyData.newBuilder(PartyDataType.PRIVACY_SETTINGS)
                .setPartyType(configuration.getPrivacyType().getName())
                .setPartyInviteRestriction(configuration.allowFriendsOfFriends() ? "AnyMember" : "LeaderOnly")
                .setOnlyLeaderFriendsCanJoin(!configuration.allowFriendsOfFriends());

        final var dataPacket = new PacketPartyData(this, dataBuilder.build());
        service.sendPacketTo(configurationPacket, to);
        service.sendPacketTo(dataPacket, to);
    }

    /**
     * Sends the current configuration to all party members
     *
     * @param service the party service
     */
    public void sendPrivacyConfigurationToAll(PartyService service) {
        members.forEach(member -> sendPrivacyConfigurationTo(service, member.getJid()));
    }

    /**
     * Sends the current configuration to all members in the collection
     *
     * @param service the service
     * @param members a list of members in the party
     */
    public void sendPrivacyConfigurationToAll(PartyService service, Collection<PartyMember> members) {
        members.forEach(member -> sendPrivacyConfigurationTo(service, member.getJid()));
    }

    public PartyConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Creates a 'fake' party.
     * Generates a random 32 character key and party ID.
     *
     * @param configuration the configuration for the party
     * @return a new {@link Party} instance with the desired configuration
     */
    public static Party createParty(PartyConfiguration configuration) {
        final var partyId = RandomStringUtils.randomAlphanumeric(32).toUpperCase();
        final var accessKey = RandomStringUtils.randomAlphanumeric(32).toUpperCase();
        return new Builder(partyId, accessKey, configuration).build();
    }

    /**
     * @param payload the payload received
     * @param from    who it was sent from
     * @return a new {@link Builder} instance
     */
    public static Builder newBuilder(JsonObject payload, Jid from) {
        return new Builder(payload, from);
    }

    public static final class Builder {

        private JsonObject payload;
        private String id, accessKey;
        private Jid from;

        private long presencePermissions = -1;
        private int invitePermissions = -1, partyFlags = -1, notAcceptingMembersReason = -1, maxMembers = -1;

        private Builder(JsonObject payload, Jid from) {
            this.payload = payload;
            this.from = from;

            parsePayload();
        }

        private Builder(String id, String accessKey, PartyConfiguration configuration) {
            this.id = id;
            this.accessKey = accessKey;
            this.presencePermissions = configuration.getPresencePermissions();
            this.invitePermissions = configuration.getPermissions().getInvitePermissions();
            this.partyFlags = configuration.getPermissions().getPartyFlags();
            this.notAcceptingMembersReason = configuration.getPermissions().getNotAcceptingMembersReason();
            this.maxMembers = configuration.getMaxMembers();
        }

        private void parsePayload() {
            id = JsonUtility.getString("partyId", payload).orElse("");
            accessKey = JsonUtility.getString("accessKey", payload).orElse(null);

            JsonUtility.getLong("presencePermissions", payload).ifPresent(presencePermissions -> this.presencePermissions = presencePermissions);
            JsonUtility.getInt("invitePermissions", payload).ifPresent(value -> invitePermissions = value);
            JsonUtility.getInt("partyFlags", payload).ifPresent(value -> partyFlags = value);
            JsonUtility.getInt("notAcceptingMembersReason", payload).ifPresent(value -> notAcceptingMembersReason = value);
            JsonUtility.getInt("maxMembers", payload).ifPresent(value -> maxMembers = value);
        }

        public Party build() {
            return new Party(this);
        }
    }

}
