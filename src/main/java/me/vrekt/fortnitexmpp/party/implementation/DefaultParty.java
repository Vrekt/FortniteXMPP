package me.vrekt.fortnitexmpp.party.implementation;

import me.vrekt.fortnitexmpp.party.PartyResource;
import me.vrekt.fortnitexmpp.party.implementation.configuration.PartyConfiguration;
import me.vrekt.fortnitexmpp.party.implementation.member.PartyMember;
import me.vrekt.fortnitexmpp.party.implementation.request.configuration.PartyUpdateConfiguration;
import me.vrekt.fortnitexmpp.party.implementation.request.data.PartyData;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.apache.commons.lang3.RandomStringUtils;

import javax.json.JsonObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class DefaultParty implements Party {

    /**
     * Build ID, updated with newer fortnite versions
     */
    public static int buildId = 5311236;

    private final List<PartyMember> members = new CopyOnWriteArrayList<>();
    private final String partyId, accessKey;

    private PartyConfiguration configuration;
    private PartyMember partyLeader;

    /**
     * Creates a new {@link Party} from the provided configuration.
     *
     * @param configuration the configuration
     */
    DefaultParty(PartyConfiguration configuration) {
        this.partyId = RandomStringUtils.randomAlphanumeric(32).toUpperCase();
        this.accessKey = RandomStringUtils.randomAlphanumeric(32).toUpperCase();
        this.configuration = configuration;
    }

    /**
     * Creates a new {@link Party} from the provided payload.
     *
     * @param payload the payload
     */
    DefaultParty(JsonObject payload) {
        this.partyId = JsonUtility.getString("partyId", payload).orElse("");
        this.accessKey = JsonUtility.getString("accessKey", payload).orElse("");
        final var presencePermissions = JsonUtility.getLong("presencePermissions", payload).orElse(-1L);
        final var invitePermissions = JsonUtility.getInt("invitePermissions", payload).orElse(-1);
        final var partyFlags = JsonUtility.getInt("partyFlags", payload).orElse(-1);
        final var notAcceptingMembersReason = JsonUtility.getInt("notAcceptingMembersReason", payload).orElse(-1);
        final var maxMembers = JsonUtility.getInt("maxMembers", payload).orElse(4);
        this.configuration = new PartyConfiguration(invitePermissions, partyFlags, notAcceptingMembersReason, maxMembers, presencePermissions);
    }

    @Override
    public String partyId() {
        return partyId;
    }

    @Override
    public String accessKey() {
        return accessKey;
    }

    @Override
    public PartyMember leader() {
        return partyLeader;
    }

    @Override
    public List<PartyMember> members() {
        return members;
    }

    @Override
    public void addMember(PartyMember member) {
        // check if this member already exists.
        if (members.stream().anyMatch(member1 -> member1.accountId().equals(member.accountId()))) return;
        members.add(member);
    }

    @Override
    public void removeMember(PartyMember member) {
        members.remove(member);
    }

    @Override
    public void removeMemberById(String accountId) {
        members.removeIf(member -> member.accountId().equals(accountId));
    }

    @Override
    public void setLeader(PartyMember member) {
        this.partyLeader = member;
    }

    @Override
    public Party updateConfigurationAndSend(PartyResource resource, PartyConfiguration configuration) {
        this.configuration = configuration;

        resource.sendRequestTo(new PartyUpdateConfiguration(this, configuration), members);
        resource.sendRequestTo(PartyData.forNewPrivacySettings(configuration, partyId), members);
        return this;
    }

    @Override
    public Party updateConfiguration(PartyConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public PartyConfiguration configuration() {
        return configuration;
    }

}
