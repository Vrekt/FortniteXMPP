package me.vrekt.fortnitexmpp.implementation.presence;

import org.jxmpp.jid.Jid;

import java.util.Optional;

public interface FortnitePresence {

    /**
     * @return the account id associated with the presence.
     */
    String getAccountId();

    /**
     * @return a session ID, empty if the account is not in a match.
     */
    Optional<String> getSessionId();

    /**
     * @return the ID of the party, if any
     */
    Optional<String> getPartyId();

    /**
     * @return the access key used to join the party, if any
     */
    Optional<String> getAccessKey();

    /**
     * @return the status of the account. {@code null} if the status is empty. (they are in launcher, etc)
     */
    String getStatus();

    /**
     * @return the amount of players remaining in the match.
     * This is not accurate and is only updated every 4-5 minutes or so.
     */
    int getPlayersRemaining();

    /**
     * @return the size of their party.
     */
    int getPartySize();

    /**
     * @return if the account is playing or not.
     */
    boolean isPlaying();

    /**
     * @return Who the presence was sent from
     */
    Jid getFrom();

}
