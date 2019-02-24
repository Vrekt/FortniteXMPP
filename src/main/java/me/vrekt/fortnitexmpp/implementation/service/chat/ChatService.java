package me.vrekt.fortnitexmpp.implementation.service.chat;

import me.vrekt.fortnitexmpp.implementation.chat.IncomingMessageListener;
import me.vrekt.fortnitexmpp.implementation.service.Service;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jxmpp.jid.EntityBareJid;

public interface ChatService extends Service {

    /**
     * Add an incoming message listener.
     * This is invoked when a message is received.
     *
     * @param listener the listener
     */
    void addIncomingMessageListener(IncomingMessageListener listener);

    /**
     * Remove an incoming message listener.
     *
     * @param listener the listener
     */
    void removeIncomingMessageListener(IncomingMessageListener listener);

    /**
     * Send a message to an account.
     *
     * @param accountId the ID of the account
     * @param message   the message
     * @return false if sending failed.
     */
    boolean sendMessage(String accountId, String message);

    /**
     * Send a message to a user.
     *
     * @param user    the user as a bare JID. (<account id>@prod.ol.epicgames.com)
     * @param message the message
     * @return false if sending failed.
     */
    boolean sendMessage(EntityBareJid user, String message);

    /**
     * @return Smacks internal chat manager
     */
    ChatManager getChatManager();

}
