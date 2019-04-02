package me.vrekt.fortnitexmpp.presence.implementation.listener;

import me.vrekt.fortnitexmpp.presence.implementation.FortnitePresence;

public interface FortnitePresenceHandler {

    /**
     * @return true if this handler is active, false otherwise.
     */
    boolean isActive();

    /**
     * @return true if this handler is ready to handle presences.
     */
    boolean isReady();

    /**
     * @param accountId the account ID of the presence
     * @return true if this account ID is relevant
     */
    boolean isRelevant(final String accountId);

    /**
     * Handle the presence
     *
     * @param presence the presence
     */
    void handlePresence(final FortnitePresence presence);

}
