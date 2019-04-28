package me.vrekt.fortnitexmpp.chat;

import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.chat.implementation.IncomingMessageListener;
import org.jxmpp.jid.Jid;

public interface ChatResource extends AutoCloseable {

    /**
     * Adds an {@link IncomingMessageListener}
     *
     * @param listener the listener
     */
    boolean addIncomingMessageListener(IncomingMessageListener listener);

    /**
     * Removes an {@link IncomingMessageListener}
     *
     * @param listener the listener
     */
    boolean removeIncomingMessageListener(final IncomingMessageListener listener);

    /**
     * Attempts to send a message to the provided {@code accountId}
     *
     * @param accountId the account ID of the user you want to message.
     * @param message   the message to send
     * @return {@code true} if sending the message was successful.
     */
    boolean sendMessage(final String accountId, final String message);

    /**
     * Attempts to send a message to the provided {@code EntityBareJid}
     *
     * @param user    the user to send it to.
     * @param message the message to send
     * @return {@code true} if sending the message was successful.
     */
    boolean sendMessage(final Jid user, final String message);

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
