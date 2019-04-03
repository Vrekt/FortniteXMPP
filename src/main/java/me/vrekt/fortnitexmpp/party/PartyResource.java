package me.vrekt.fortnitexmpp.party;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.listener.PartyListener;
import me.vrekt.fortnitexmpp.party.implementation.member.PartyMember;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import org.jxmpp.jid.Jid;

import java.util.Collection;
import java.util.Optional;

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
     * Internally there is a party instance kept updated and only will get cleared when a new packet is received with a ID and key.
     *
     * @return the internal party context
     */
    Optional<Party> partyContext();

}
