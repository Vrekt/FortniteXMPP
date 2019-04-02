package me.vrekt.fortnitexmpp.chat;

import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.chat.implementation.IncomingMessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class DefaultChatResource implements ChatResource {

    private final List<IncomingMessageListener> listeners = new CopyOnWriteArrayList<>();
    private final MessageListener messageListener = new MessageListener();
    private final ChatManager chatManager;

    /**
     * Initialize this resource.
     *
     * @param fortniteXMPP the instance of {@link FortniteXMPP}
     */
    public DefaultChatResource(final FortniteXMPP fortniteXMPP) {
        chatManager = ChatManager.getInstanceFor(fortniteXMPP.connection());
        chatManager.addIncomingListener(messageListener);
    }

    @Override
    public boolean addIncomingMessageListener(final IncomingMessageListener listener) {
        return listeners.add(listener);
    }

    @Override
    public boolean removeIncomingMessageListener(final IncomingMessageListener listener) {
        return listeners.remove(listener);
    }

    @Override
    public boolean sendMessage(final String accountId, final String message) {
        EntityBareJid to = JidCreate.entityBareFromOrThrowUnchecked(accountId + "@" + FortniteXMPP.SERVICE_DOMAIN);
        return sendMessage(to, message);
    }

    @Override
    public boolean sendMessage(final EntityBareJid user, final String message) {
        try {
            chatManager.chatWith(user).send(message);
        } catch (final SmackException.NotConnectedException | InterruptedException exception) {
            return false;
        }
        return true;
    }

    @Override
    public ChatManager chatManager() {
        return chatManager;
    }

    @Override
    public void close() {
        chatManager.removeIncomingListener(messageListener);
        listeners.clear();
    }

    /**
     * Used for listening to incoming messages.
     * Once an incoming message is received all the listeners registered are fired.
     */
    private final class MessageListener implements IncomingChatMessageListener {
        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            listeners.forEach(listener -> listener.messageReceived(new me.vrekt.fortnitexmpp.chat.implementation.Message(from, message.getBody(), chat)));
        }
    }

}
