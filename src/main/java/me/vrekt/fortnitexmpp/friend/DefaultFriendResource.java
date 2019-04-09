package me.vrekt.fortnitexmpp.friend;

import com.google.common.flogger.FluentLogger;
import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.friend.implementation.FriendListener;
import me.vrekt.fortnitexmpp.friend.type.FriendType;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import me.vrekt.fortnitexmpp.utility.Logging;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import javax.json.Json;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class DefaultFriendResource implements FriendResource {

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();
    private final List<FriendListener> listeners = new CopyOnWriteArrayList<>();
    private final MessageListener messageListener = new MessageListener();
    private XMPPTCPConnection connection;
    private FortniteXMPP fortniteXMPP;

    private boolean enableLogging;

    /**
     * Initialize this resource
     *
     * @param fortniteXMPP the {@link FortniteXMPP} instance
     */
    public DefaultFriendResource(final FortniteXMPP fortniteXMPP, final boolean enableLogging) {
        this.connection = fortniteXMPP.connection();
        this.fortniteXMPP = fortniteXMPP;
        this.enableLogging = enableLogging;
        connection.addAsyncStanzaListener(messageListener, StanzaTypeFilter.MESSAGE);
    }

    @Override
    public void addFriendListener(final FriendListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeFriendListener(final FriendListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean acceptOrSendFriendRequest(final String accountId) {
        Objects.requireNonNull(accountId, "Account ID cannot be null.");
        try {
            fortniteXMPP.fortnite().friend().addOneByAccountId(accountId);
            Logging.logInfoIfApplicable(LOGGER.atInfo(), enableLogging, "Added or sent a friend request to: " + accountId);
        } catch (final IOException exception) {
            LOGGER.atWarning().withCause(exception).log("Could not send or accept friend request to: " + accountId);
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        connection.removeAsyncStanzaListener(messageListener);
        listeners.clear();
        connection = null;
        fortniteXMPP = null;
    }

    @Override
    public void disposeConnection() {
        connection.removeAsyncStanzaListener(messageListener);
    }

    @Override
    public void reinitialize(final FortniteXMPP fortniteXMPP) {
        this.connection = fortniteXMPP.connection();
        this.fortniteXMPP = fortniteXMPP;
        connection.addAsyncStanzaListener(messageListener, StanzaTypeFilter.MESSAGE);
    }

    /**
     * Listens for {@link me.vrekt.fortnitexmpp.friend.type.FriendType} related messages.
     */
    private final class MessageListener implements StanzaListener {
        @Override
        public void processStanza(final Stanza packet) {
            final var message = (Message) packet;
            if (message.getType() != Message.Type.normal) return;

            try {
                final var reader = Json.createReader(new StringReader(message.getBody()));
                final var data = reader.readObject();
                reader.close();

                final var type = FriendType.typeOf(JsonUtility.getString("type", data).orElse(null));
                if (type == null) return; // not relevant

                Logging.logInfoIfApplicable(LOGGER.atInfo(), enableLogging, "Friend message type: " + type.getName() + "\nWith payload: " + data.toString());

                listeners.forEach(listener -> listener.onXMPPFriendMessage(message));

                // a different type of friend object, sent at login and when a friend request action is sent
                if (type == FriendType.FRIEND) {
                    final var payload = data.getJsonObject("payload");
                    final var direction = payload.getString("direction");
                    final var status = payload.getString("status");
                    final var accountId = payload.getString("accountId");

                    if (direction.equals("OUTBOUND")) {
                        // the current account sent the request
                    } else if (direction.equals("INBOUND")) {
                        // somebody else sent the request.
                        switch (status) {
                            case "PENDING":
                                listeners.forEach(listener -> listener.onFriendRequestReceived(accountId));
                                break;
                            case "DELETED":
                                listeners.forEach(listener -> listener.onFriendRequestDeleted(accountId));
                                break;
                        }
                    }
                    // the normal types.
                } else if (type == FriendType.FRIENDSHIP_REQUEST || type == FriendType.FRIENDSHIP_REMOVE) {
                    final var status = data.containsKey("status") ? data.getString("status") : data.getString("reason");
                    final var accountId = data.getString("to");
                    switch (status) {
                        case "ABORTED":
                            listeners.forEach(listener -> listener.onFriendRequestAborted(accountId));
                            break;
                        case "ACCEPTED":
                            listeners.forEach(listener -> listener.onFriendRequestAccepted(accountId));
                            break;
                        case "REJECTED":
                            listeners.forEach(listener -> listener.onFriendRequestRejected(accountId));
                            break;
                    }
                }

            } catch (final Exception exception) {
                LOGGER.atWarning().log("Failed to parse message JSON. from: " + packet.getFrom().asUnescapedString());
            }
        }
    }


}
