package me.vrekt.fortnitexmpp.implementation.service.presence;

import me.vrekt.fortnitexmpp.implementation.presence.FortnitePresenceListener;
import me.vrekt.fortnitexmpp.implementation.presence.PresenceHandler;
import me.vrekt.fortnitexmpp.implementation.presence.implementation.DefaultFortnitePresence;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.Jid;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultPresenceService implements PresenceService {

    private final Set<FortnitePresenceListener> listeners = ConcurrentHashMap.newKeySet();
    private final Set<PresenceHandler> handlers = ConcurrentHashMap.newKeySet();
    private final Set<Jid> addresses = ConcurrentHashMap.newKeySet();
    private final XMPPTCPConnection connection;
    private final Roster roster;

    private final PacketListener packetListener;
    private final AddressListener addressListener;

    private boolean onlyHandleAvailablePresences;

    public DefaultPresenceService(XMPPTCPConnection connection) {
        this.connection = connection;
        onlyHandleAvailablePresences = true;

        packetListener = new PacketListener();
        addressListener = new AddressListener();

        roster = Roster.getInstanceFor(connection);
        roster.addRosterListener(addressListener);

        connection.addAsyncStanzaListener(packetListener, new StanzaTypeFilter(Presence.class));
        if (!roster.isLoaded()) {
            try {
                roster.reloadAndWait();
            } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void addPresenceListener(FortnitePresenceListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removePresenceListener(FortnitePresenceListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void addPresenceHandler(PresenceHandler presenceHandler) {
        handlers.add(presenceHandler);
    }

    @Override
    public void removePresenceHandler(PresenceHandler presenceHandler) {
        handlers.remove(presenceHandler);
    }

    @Override
    public void reloadRoster(boolean wait) {
        try {
            if (wait) {
                roster.reloadAndWait();
            } else {
                roster.reload();
            }
        } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void handlePreviouslyCollectedPresences() {
        addresses.forEach(jid -> handlePresence(roster.getPresence(jid.asBareJid())));
    }

    @Override
    public void setOnlyHandleAvailablePresences(boolean onlyHandleAvailablePresences) {
        this.onlyHandleAvailablePresences = onlyHandleAvailablePresences;
    }

    @Override
    public Roster getRoster() {
        return roster;
    }

    @Override
    public void close() {
        connection.removeAsyncStanzaListener(packetListener);
        roster.removeRosterListener(addressListener);
        listeners.clear();
        handlers.clear();
        addresses.clear();
    }

    /**
     * Parse the presence
     *
     * @param presence the presence
     */
    private void handlePresence(Presence presence) {
        final var localPart = presence.getFrom().getLocalpartOrNull();
        if (localPart == null) return;

        final var accountId = localPart.asUnescapedString();
        if (presence.getStatus() == null) return;

        final var fortnitePresence = new DefaultFortnitePresence(accountId, presence.getStatus(), presence.getFrom());
        listeners.forEach(listener -> listener.handlePresence(fortnitePresence));
        handlers.stream().filter(handler -> handler.isActive() && handler.isReady() && handler.isRelevant(accountId)).forEach(handler -> handler.handlePresence(fortnitePresence));
    }

    private final class PacketListener implements StanzaListener {
        @Override
        public void processStanza(Stanza packet) {
            if (!(packet instanceof Presence)) return;
            final var presence = (Presence) packet;
            if (onlyHandleAvailablePresences && !presence.isAvailable()) return;
            handlePresence(presence);
        }
    }

    private final class AddressListener implements RosterListener {
        @Override
        public void entriesAdded(Collection<Jid> addresses) {
            DefaultPresenceService.this.addresses.addAll(addresses);
        }

        @Override
        public void entriesUpdated(Collection<Jid> addresses) {
            DefaultPresenceService.this.addresses.addAll(addresses);
        }

        @Override
        public void entriesDeleted(Collection<Jid> addresses) {
            DefaultPresenceService.this.addresses.removeAll(addresses);
        }

        @Override
        public void presenceChanged(Presence presence) {
            handlePresence(presence);
        }
    }

}
