package me.vrekt.fortnitexmpp.implementation.presence;

public interface PresenceHandler {

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
    boolean isRelevant(String accountId);

    /**
     * Handle the presence
     *
     * @param presence the presence
     */
    void handlePresence(FortnitePresence presence);

}
