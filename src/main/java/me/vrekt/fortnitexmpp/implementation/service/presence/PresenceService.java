package me.vrekt.fortnitexmpp.implementation.service.presence;

import me.vrekt.fortnitexmpp.implementation.presence.FortnitePresenceListener;
import me.vrekt.fortnitexmpp.implementation.presence.PresenceHandler;
import me.vrekt.fortnitexmpp.implementation.service.Service;
import org.jivesoftware.smack.roster.Roster;

public interface PresenceService extends Service {

    /**
     * Add a presence listener
     *
     * @param listener the listener
     */
    void addPresenceListener(FortnitePresenceListener listener);

    /**
     * Remove a presence listener
     *
     * @param listener the listener
     */
    void removePresenceListener(FortnitePresenceListener listener);

    /**
     * Add a handler for presences
     *
     * @param presenceHandler the handler
     */
    void addPresenceHandler(PresenceHandler presenceHandler);

    /**
     * Remove a handler for presences.
     *
     * @param presenceHandler the handler
     */
    void removePresenceHandler(PresenceHandler presenceHandler);

    /**
     * Reload the roster
     *
     * @param wait if true roster reloading will not be performed async and will block.
     */
    void reloadRoster(boolean wait);

    /**
     * When a roster is first loaded all addresses are added to a set
     * calling this function will iterate through that set and get the current presence
     * for the address and then handle it
     */
    void handlePreviouslyCollectedPresences();

    /**
     * @param onlyHandleAvailablePresences if true only presences who's status are 'available' will be handled
     */
    void setOnlyHandleAvailablePresences(boolean onlyHandleAvailablePresences);

    /**
     * @return the roster
     */
    Roster getRoster();

}
