package me.vrekt.fortnitexmpp.party;

import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.listener.PartyListener;
import me.vrekt.fortnitexmpp.party.implementation.member.PartyMember;
import me.vrekt.fortnitexmpp.party.implementation.presence.PartyPresence;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jxmpp.jid.Jid;

import java.util.Collection;

public interface PartyResource extends AutoCloseable {

    /**
     * Adds a party listener
     *
     * @param listener the listener
     */
    void addPartyListener(final PartyListener listener);

    /**
     * Removes a party listener
     *
     * @param listener the listener
     */
    void removePartyListener(final PartyListener listener);

    /**
     * Sends a request to the provided {@code recipient}
     *
     * @param request   the request
     * @param recipient the recipient
     */
    void sendRequestTo(final PartyRequest request, final Jid recipient);

    /**
     * Sends a request to the provided {@code recipients}
     *
     * @param request    the request
     * @param recipients the recipients
     */
    void sendRequestTo(final PartyRequest request, final Collection<Jid> recipients);

    /**
     * Sends a request to the provided {@code members}
     *
     * @param request the request
     * @param members the list of members
     */
    void sendRequestTo(final PartyRequest request, final Iterable<PartyMember> members);

    /**
     * Tries to send a request to the provided {@code recipient}
     *
     * @param request   the request
     * @param recipient the recipient
     * @return {@code true} if no exception occurred while sending.
     */
    boolean trySendRequestTo(final PartyRequest request, final Jid recipient);

    /**
     * Tries to send a request to the provided {@code recipients}
     *
     * @param request    the request
     * @param recipients the recipient
     * @return {@code true} if no exception occurred while sending.
     */
    boolean trySendRequestTo(final PartyRequest request, final Collection<Jid> recipients);

    /**
     * Tries to send a request to the provided {@code recipients}
     *
     * @param request the request
     * @param members the list of members
     * @return {@code true} if no exception occurred while sending.
     */
    boolean trySendRequestTo(final PartyRequest request, final Iterable<PartyMember> members);

    /**
     * @param partyId the ID of the party
     * @return the party that has the ID or {@code null} if no party was found
     */
    Party getPartyById(final String partyId);

    /**
     * Attempts to remove a party by the ID.
     *
     * @param partyId the ID of the party
     */
    void removePartyById(final String partyId);

    /**
     * Set your presence to the party
     *
     * @param presence the presence to use
     */
    void setPartyPresence(PartyPresence presence);

    /**
     * Sends a message to the party
     *
     * @param message the message
     * @return {@code true} if successful.
     */
    boolean sendMessageToParty(final Party party, final String message);

    /**
     * Sends a message to the party
     *
     * @param partyId the ID of the party
     * @param message the message
     * @return {@code true} if successful.
     */
    boolean sendMessageToParty(final String partyId, final String message);

    /**
     * Get the {@link MultiUserChat} for the party
     *
     * @param party the party
     * @return the {@link MultiUserChat} or {@code null} if not found
     */
    MultiUserChat getChatForParty(final Party party);

    /**
     * Get the {@link MultiUserChat} for the party
     *
     * @param partyId the ID of the party
     * @return the {@link MultiUserChat} or {@code null} if not found
     */
    MultiUserChat getChatForParty(final String partyId);

    /**
     * Removes stanza listeners but does not clear internal listeners.
     */
    void disposeConnection();

    /**
     * Reinitialize this resource for the new connect.
     *
     * @param fortniteXMPP the {@link FortniteXMPP} instance
     */
    void reinitialize(final FortniteXMPP fortniteXMPP);

}
