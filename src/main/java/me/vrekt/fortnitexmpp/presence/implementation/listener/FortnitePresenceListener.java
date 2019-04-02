package me.vrekt.fortnitexmpp.presence.implementation.listener;

import me.vrekt.fortnitexmpp.presence.implementation.FortnitePresence;

public interface FortnitePresenceListener {

    /**
     * Invoked when a new {@link FortnitePresence} is received.
     *
     * @param presence the presence
     */
    void presenceReceived(final FortnitePresence presence);

}
