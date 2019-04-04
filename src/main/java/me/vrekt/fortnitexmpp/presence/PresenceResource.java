package me.vrekt.fortnitexmpp.presence;

import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.presence.implementation.listener.FortnitePresenceHandler;
import me.vrekt.fortnitexmpp.presence.implementation.listener.FortnitePresenceListener;
import org.jivesoftware.smack.roster.Roster;

public interface PresenceResource extends AutoCloseable {

    /**
     * Add a presence listener
     *
     * @param listener the listener
     */
    void addPresenceListener(final FortnitePresenceListener listener);

    /**
     * Remove a presence listener
     *
     * @param listener the listener
     */
    void removePresenceListener(final FortnitePresenceListener listener);

    /**
     * Add a handler for presences
     *
     * @param handler the handler
     */
    void addPresenceHandler(final FortnitePresenceHandler handler);

    /**
     * Remove a handler for presences.
     *
     * @param handler handler
     */
    void removePresenceHandler(final FortnitePresenceHandler handler);

    /**
     * Reload the roster
     *
     * @param wait if {@code true} this method will block until the roster is reloaded.
     */
    void reloadRoster(boolean wait);

    /**
     * Iterates through all roster entries and invokes the listeners
     */
    void handleAllRosterEntries();

    /**
     * @param onlyHandleAvailablePresences {@code true} if only accounts who are available should be handled.
     */
    void setOnlyHandleAvailablePresences(boolean onlyHandleAvailablePresences);

    /**
     * @return the roster
     */
    Roster roster();

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
