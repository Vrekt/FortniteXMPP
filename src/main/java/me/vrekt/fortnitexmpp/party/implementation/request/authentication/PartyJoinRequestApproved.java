package me.vrekt.fortnitexmpp.party.implementation.request.authentication;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.configuration.PartyConfiguration;
import me.vrekt.fortnitexmpp.party.implementation.member.PartyMember;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;
import java.util.Collection;

public final class PartyJoinRequestApproved implements PartyRequest {

    private final String payload;

    /**
     * Initialize this request
     *
     * @param party the party
     */
    public PartyJoinRequestApproved(final Party party) {
        this(party.partyId(), party.accessKey(), party.members(), party.configuration());
    }

    /**
     * Initialize this request
     *
     * @param partyId       the ID of the party
     * @param accessKey     the access key used to join the party
     * @param memberList    the list of members in this party already
     * @param configuration the configuration
     */
    public PartyJoinRequestApproved(final String partyId, final String accessKey, final Collection<PartyMember> memberList, final PartyConfiguration configuration) {
        final var payload = Json.createObjectBuilder()
                .add("partyId", partyId)
                .add("presencePermissions", configuration.presencePermissions())
                .add("invitePermissions", configuration.invitePermissions())
                .add("partyFlags", configuration.partyFlags())
                .add("notAcceptingMembersReason", configuration.notAcceptingMembersReason())
                .add("maxMembers", configuration.maxMembers())
                .add("password", "")
                .add("accessKey", accessKey);
        final var membersArray = Json.createArrayBuilder();
        memberList.forEach(member -> {
            final var m = Json.createObjectBuilder();
            m.add("userId", member.accountId());
            m.add("xmppResource", member.resource());
            m.add("displayName", member.displayName());
            membersArray.add(m);
        });

        payload.add("members", membersArray.build());
        this.payload = RequestBuilder.buildRequest(payload.build(), PartyType.PARTY_JOIN_REQUEST_APPROVED).toString();
    }

    @Override
    public String payload() {
        return payload;
    }
}
