package me.vrekt.fortnitexmpp.implementation.service.friend;

import me.vrekt.fortnitexmpp.FortniteXmpp;
import me.vrekt.fortnitexmpp.implementation.friend.FriendListener;
import me.vrekt.fortnitexmpp.implementation.friend.FriendPacketType;
import me.vrekt.fortnitexmpp.implementation.friend.FriendRequest;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultFriendService implements FriendService {

    private final Set<FriendListener> listeners = ConcurrentHashMap.newKeySet();
    private final PacketListener packetListener;
    private final XMPPTCPConnection connection;
    private final FortniteXmpp fortniteXmpp;

    public DefaultFriendService(final FortniteXmpp fortniteXmpp) {
        this.fortniteXmpp = fortniteXmpp;
        this.connection = fortniteXmpp.getConnection();

        packetListener = new PacketListener();
        connection.addAsyncStanzaListener(packetListener, new StanzaTypeFilter(Message.class));
    }

    @Override
    public void addFriendListener(FriendListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeFriendListener(FriendListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void close() {
        connection.removeAsyncStanzaListener(packetListener);
        listeners.clear();
    }

    private void accept(String accountId) {
        try {
            fortniteXmpp.getFortnite().friend().addOneByAccountId(accountId);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private final class PacketListener implements StanzaListener {

        @Override
        public void processStanza(Stanza packet) {
            try {
                if (!(packet instanceof Message)) return;
                Message message = (Message) packet;
                if (message.getType() != Message.Type.normal) return;

                var reader = Json.createReader(new StringReader(message.getBody()));
                var data = reader.readObject();
                reader.close();

                var typeParse = JsonUtility.getString("type", data).orElse("UNKNOWN");
                var type = FriendPacketType.from(typeParse);
                if (type == FriendPacketType.UNKNOWN) return;

                listeners.forEach(listener -> listener.onFriendMessage(message));
                postFriendEvent(data, type);
            } catch (Exception exception) {
                //
            }

        }
    }

    private void postFriendEvent(JsonObject payload, FriendPacketType type) {
        switch (type) {
            case FRIENDSHIP_REQUEST:
                var status = FriendRequest.Status.valueOf(payload.getString("status"));
                handleStatus(payload, status);
                break;
            case FRIENDSHIP_REMOVE:
                var reason = FriendRequest.Status.valueOf(payload.getString("reason"));
                handleStatus(payload, reason);
                break;
        }

    }

    private void handleStatus(JsonObject payload, FriendRequest.Status status) {
        final var from = payload.getString("from");
        switch (status) {
            case PENDING:
                listeners.forEach(listener -> listener.onFriendRequestReceived(FriendRequest.newInstance(payload, this::accept)));
                break;
            case ABORTED:
                listeners.forEach(listener -> listener.onFriendRequestAborted(from));
                break;
            case DELETED:
                listeners.forEach(listener -> listener.onFriendRequestDeleted(from));
                break;
            case ACCEPTED:
                listeners.forEach(listener -> listener.onFriendRequestAccepted(from));
                break;
            case REJECTED:
                listeners.forEach(listener -> listener.onFriendRequestRejected(from));
                break;
        }
    }

}
