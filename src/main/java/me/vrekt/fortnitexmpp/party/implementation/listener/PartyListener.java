package me.vrekt.fortnitexmpp.party.implementation.listener;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.configuration.PartyConfiguration;
import me.vrekt.fortnitexmpp.party.implementation.data.ImmutablePartyData;
import me.vrekt.fortnitexmpp.party.implementation.member.PartyMember;
import me.vrekt.fortnitexmpp.party.implementation.member.data.ImmutablePartyMemberData;
import me.vrekt.fortnitexmpp.party.implementation.request.general.InvitationResponse;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.Jid;

import java.util.Set;

public interface PartyListener {

    default void onInvitation(final Party party, final Jid from) {

    }

    default void onInvitationResponse(final Party party, final InvitationResponse response, final Jid from) {

    }

    default void onQueryJoinability(final Party party, final int crossplayPreference, final Jid from) {

    }

    default void onQueryJoinabilityResponse(final Party party, final boolean isJoinable, final int rejectionType, final String resultParam, final Jid from) {

    }

    default void onJoinRequest(final Party party, final PartyMember member, final Jid from) {

    }

    default void onJoinRequestApproved(final Party party, final Set<PartyMember> members, final Jid from) {

    }

    default void onJoinRequestRejected(final Party party, final Jid from) {

    }

    default void onJoinAcknowledged(final Party party, final Jid from) {

    }

    default void onJoinAcknowledgedResponse(final Party party, final Jid from) {

    }

    default void onPartyMemberDataReceived(final Party party, final ImmutablePartyMemberData data, final Jid from) {

    }

    default void onPartyMemberJoined(final Party party, final PartyMember member, final Jid from) {

    }

    default void onPartyMemberExited(final Party party, final String accountId, final boolean wasKicked, final Jid from) {

    }

    default void onPartyMemberPromoted(final Party party, final String accountId, final boolean wasFromLeaderLeaving, final Jid from) {

    }

    default void onPartyConfigurationUpdated(final Party party, final PartyConfiguration configuration, final Jid from) {

    }

    default void onPartyData(final Party party, final ImmutablePartyData data, final Jid from) {

    }

    default void onMessageReceived(final Message message) {

    }

}
