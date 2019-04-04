package me.vrekt.fortnitexmpp.chat;

import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.chat.implementation.IncomingMessageListener;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jxmpp.jid.EntityBareJid;

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
    boolean removeIncomingMessageListener(IncomingMessageListener listener);

    /**
     * Attempts to send a message to the provided {@code accountId}
     *
     * @param accountId the account ID of the user you want to message.
     * @param message   the message to send
     * @return {@code true} if sending the message was successful.
     */
    boolean sendMessage(String accountId, String message);

    /**
     * Attempts to send a message to the provided {@code EntityBareJid}
     *
     * @param user    the user to send it to.
     * @param message the message to send
     * @return {@code true} if sending the message was successful.
     */
    boolean sendMessage(EntityBareJid user, String message);

    /**
     * @return the internal instance of {@link ChatManager}
     */
    ChatManager chatManager();

    /**
     * Removes stanza listeners but does not clear internal listeners.
     */
    void closeDirty();

    /**
     * Reinitialize this resource for the new connect.
     *
     * @param fortniteXMPP the {@link FortniteXMPP} instance
     */
    void reinitialize(final FortniteXMPP fortniteXMPP);

}
