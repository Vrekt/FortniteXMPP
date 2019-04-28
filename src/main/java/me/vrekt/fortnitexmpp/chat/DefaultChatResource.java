package me.vrekt.fortnitexmpp.chat;

import com.google.common.flogger.FluentLogger;
import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.chat.implementation.IncomingMessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class DefaultChatResource implements ChatResource {
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private final List<IncomingMessageListener> listeners = new CopyOnWriteArrayList<>();
    private final MessageListener messageListener = new MessageListener();

    private XMPPTCPConnection connection;

    /**
     * Initialize this resource.
     *
     * @param fortniteXMPP the instance of {@link FortniteXMPP}
     */
    public DefaultChatResource(final FortniteXMPP fortniteXMPP) {
        this.connection = fortniteXMPP.connection();
        connection.addAsyncStanzaListener(messageListener, MessageTypeFilter.CHAT);
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
        Objects.requireNonNull(accountId, "Account ID cannot be null.");
        Objects.requireNonNull(message, "Message cannot be null.");
        final var to = JidCreate.entityBareFromOrThrowUnchecked(accountId + "@" + FortniteXMPP.SERVICE_DOMAIN);
        return sendMessage(to, message);
    }

    @Override
    public boolean sendMessage(final Jid user, final String message) {
        Objects.requireNonNull(user, "User cannot be null.");
        Objects.requireNonNull(message, "Message cannot be null.");
        try {
            final var packet = new Message(user, Message.Type.chat);
            packet.setBody(message);

            connection.sendStanza(packet);
        } catch (final SmackException.NotConnectedException | InterruptedException exception) {
            LOGGER.atWarning().withCause(exception).log("Failed to send message to: " + user.asUnescapedString());
        }
        return true;
    }

    @Override
    public void close() {
        connection.removeAsyncStanzaListener(messageListener);
        listeners.clear();
    }

    @Override
    public void disposeConnection() {
        connection.removeAsyncStanzaListener(messageListener);
    }

    @Override
    public void reinitialize(final FortniteXMPP fortniteXMPP) {
        connection = fortniteXMPP.connection();
        connection.addAsyncStanzaListener(messageListener, MessageTypeFilter.CHAT);
    }

    /**
     * Used for listening to incoming messages.
     * Once an incoming message is received all the listeners registered are fired.
     */
    private final class MessageListener implements StanzaListener {

        @Override
        public void processStanza(Stanza packet) {
            final var message = (Message) packet;
            listeners.forEach(listener -> listener.messageReceived(new me.vrekt.fortnitexmpp.chat.implementation.Message(message.getFrom(), message.getBody())));
        }
    }

}
