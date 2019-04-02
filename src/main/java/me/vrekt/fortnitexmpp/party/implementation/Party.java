package me.vrekt.fortnitexmpp.party.implementation;

import me.vrekt.fortnitexmpp.party.PartyResource;
import me.vrekt.fortnitexmpp.party.implementation.configuration.PartyConfiguration;
import me.vrekt.fortnitexmpp.party.implementation.member.PartyMember;

import javax.json.JsonObject;
import java.util.List;

public interface Party {

    int TYPE_ID = 286331153;
    String PARTY_DATA_INFO = "party.joininfodata." + TYPE_ID + "_j";

    /**
     * Static factory method for creating a new {@link Party} with the provided configuration.
     *
     * @param configuration the configuration
     * @return a new {@link Party}
     */
    static Party createParty(PartyConfiguration configuration) {
        return new DefaultParty(configuration);
    }

    /**
     * Static factory method for creating a new {@link Party} with the provided payload.
     *
     * @param payload the payload
     * @return a new {@link Party}
     */
    static Party fromPayload(JsonObject payload) {
        return new DefaultParty(payload);
    }

    /**
     * @return the unique identifier of this party.
     */
    String partyId();

    /**
     * @return the access key used to join this party.
     */
    String accessKey();

    /**
     * @return the leader of the party, or {@code null} if this is a fake party.
     */
    PartyMember leader();

    /**
     * @return a set of members who are in this party.
     */
    List<PartyMember> members();

    /**
     * Add a member to this party
     *
     * @param member the member
     */
    void addMember(PartyMember member);

    /**
     * Remove a member from this party
     *
     * @param member the member
     */
    void removeMember(PartyMember member);

    /**
     * Remove a member from this party
     *
     * @param accountId the account ID.
     */
    void removeMemberById(String accountId);

    /**
     * Set the party leader
     *
     * @param member the leader
     */
    void setLeader(PartyMember member);

    /**
     * This method will update the configuration and then send it to all party members.
     *
     * @param resource      the {@link me.vrekt.fortnitexmpp.FortniteXMPP} instance of {@link PartyResource}
     * @param configuration the configuration
     * @return this {@link Party}
     */
    Party updateConfigurationAndSend(PartyResource resource, PartyConfiguration configuration);

    /**
     * This method will update the configuration.
     *
     * @param configuration the configuration
     * @return this {@link Party}
     */
    Party updateConfiguration(PartyConfiguration configuration);

    /**
     * @return the current {@link PartyConfiguration} associated with this {@link Party}
     */
    PartyConfiguration configuration();

}
