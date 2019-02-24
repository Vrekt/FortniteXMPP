package me.vrekt.fortnitexmpp.implementation.presence;

public interface FortnitePresenceListener {

    /**
     * Handle the presence sent
     *
     * @param presence the presence
     */
    void handlePresence(FortnitePresence presence);

}
