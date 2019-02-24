package me.vrekt.fortnitexmpp.implementation.service.chat;

import me.vrekt.fortnitexmpp.implementation.chat.IncomingMessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultChatService implements ChatService {

    private final Set<IncomingMessageListener> listeners = ConcurrentHashMap.newKeySet();
    private final MessageListener messageListener;
    private final ChatManager chatManager;

    public DefaultChatService(XMPPTCPConnection connection) {
        messageListener = new MessageListener();
        chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addIncomingListener(messageListener);
    }

    @Override
    public void addIncomingMessageListener(IncomingMessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeIncomingMessageListener(IncomingMessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean sendMessage(String accountId, String message) {
        EntityBareJid to = JidCreate.entityBareFromOrThrowUnchecked(accountId + "@prod.ol.epicgames.com");
        return sendMessage(to, message);
    }

    @Override
    public boolean sendMessage(EntityBareJid user, String message) {
        try {
            chatManager.chatWith(user).send(message);
        } catch (SmackException.NotConnectedException | InterruptedException exception) {
            return false;
        }
        return true;
    }

    @Override
    public ChatManager getChatManager() {
        return chatManager;
    }

    @Override
    public void close() {
        chatManager.removeIncomingListener(messageListener);
        listeners.clear();
    }

    private final class MessageListener implements IncomingChatMessageListener {

        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            listeners.forEach(listener -> listener.onMessageReceived(new me.vrekt.fortnitexmpp.implementation.chat.message.Message(from, message.getBody(), chat)));
        }
    }

}
