package me.vrekt.fortnitexmpp.implementation.party;

import me.vrekt.fortnitexmpp.implementation.party.event.data.PartyConfigurationUpdatedEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.data.PartyDataEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.invite.PartyInvitationEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.invite.PartyInvitationResponseEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.join.PartyJoinRequestApprovedEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.join.PartyJoinRequestEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.join.PartyQueryJoinabilityEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.join.PartyQueryJoinabilityResponseEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.member.PartyMemberExitedEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.member.PartyMemberJoinedEvent;
import me.vrekt.fortnitexmpp.implementation.party.event.member.PartyMemberPromotedEvent;
import me.vrekt.fortnitexmpp.implementation.party.member.data.OtherPartyMemberData;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.Jid;

public interface PartyListener {

    /**
     * Invoked when an invitation is received.
     *
     * @param event the event
     */
    default void onInvitation(PartyInvitationEvent event) {

    }

    /**
     * Invoked when a response is received from an invite
     *
     * @param event the event
     */
    default void onInvitationResponse(PartyInvitationResponseEvent event) {

    }

    /**
     * Invoked when a join request is received.
     *
     * @param event the event
     */
    default void onJoinRequest(PartyJoinRequestEvent event) {

    }

    /**
     * Invoked when a request to join a party is approved.
     *
     * @param event the event
     */
    default void onJoinRequestApproved(PartyJoinRequestApprovedEvent event) {

    }

    /**
     * Invoked when a request to join a party is rejected.
     *
     * @param party the party
     */
    default void onJoinRequestRejected(Party party, Jid from) {

    }

    /**
     * Invoked when the approval to join was acknowledged by a client.
     *
     * @param party the party
     */
    default void onJoinAcknowledged(Party party, Jid from) {

    }

    /**
     * Invoked when you acknowledge you can join.
     *
     * @param party the party
     */
    default void onJoinAcknowledgedResponse(Party party, Jid from) {

    }

    /**
     * invoked when another player checks if they can join your party.
     *
     * @param event the event
     */
    default void onPartyQueryJoinability(PartyQueryJoinabilityEvent event) {

    }

    /**
     * Invoked when a response to check the joinability of a party is received.
     *
     * @param event the event
     */
    default void onPartyQueryJoinabilityResponse(PartyQueryJoinabilityResponseEvent event) {

    }

    /**
     * Invoked when party member data is received.
     *
     * @param party the party
     * @param data  the data.
     * @param from  who it was sent from
     */
    default void onPartyMemberData(Party party, OtherPartyMemberData data, Jid from) {

    }

    /**
     * Invoked when a party member is promoted.
     *
     * @param event the event
     */
    default void onPartyMemberPromoted(PartyMemberPromotedEvent event) {

    }

    /**
     * Invoked when a party member leaves
     *
     * @param event the event
     */
    default void onPartyMemberExited(PartyMemberExitedEvent event) {

    }

    /**
     * Invoked when a party member joins
     *
     * @param event the event
     */
    default void onPartyMemberJoined(PartyMemberJoinedEvent event) {

    }

    /**
     * Invoked when the party configuration is updated.
     *
     * @param event the event
     */
    default void onPartyConfigurationUpdated(PartyConfigurationUpdatedEvent event) {

    }

    /**
     * Invoked when party data is received.
     *
     * @param event the event
     */
    default void onPartyDataReceived(PartyDataEvent event) {

    }

    /**
     * Invoked when a message is received.
     *
     * @param message the message
     */
    default void onMessageReceived(Message message) {

    }

}
