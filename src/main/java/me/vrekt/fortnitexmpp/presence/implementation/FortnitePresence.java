package me.vrekt.fortnitexmpp.presence.implementation;

import com.google.common.flogger.FluentLogger;
import org.jxmpp.jid.Jid;

import java.util.Optional;

public interface FortnitePresence {

    FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    /**
     * Create a new {@link FortnitePresence} from the status.
     * If {@code status} is null, this method returns null.
     *
     * @param accountId the account ID of whoever sent this presence.
     * @param status    the status
     * @param from      who it was sent from
     * @return a new {@link FortnitePresence} or {@code null} if {@code status} is null
     */
    static FortnitePresence createNew(final String accountId, final String status, final Jid from) {
        if (status == null || accountId == null || from == null) return null;
        return new DefaultFortnitePresence(accountId, status, from);
    }

    /**
     * @return the account id associated with the presence.
     */
    String accountId();

    /**
     * @return a session ID, empty if the account is not in a match.
     */
    Optional<String> sessionId();

    /**
     * @return the ID of the party, if any
     */
    Optional<String> partyId();

    /**
     * @return the access key used to join the party, if any
     */
    Optional<String> accessKey();

    /**
     * @return the status of the account. {@code null} if the status is empty. (they are in launcher, etc)
     */
    String status();

    /**
     * @return the amount of players remaining in the match.
     * This is not accurate and is only updated every 4-5 minutes or so.
     */
    int playersRemaining();

    /**
     * @return the size of their party.
     */
    int partySize();

    /**
     * @return if the account is playing or not.
     */
    boolean isPlaying();

    /**
     * @return Who the presence was sent from
     */
    Jid getFrom();

    /**
     * @return {@code true} if this presence is valid and no exceptions occurred.
     */
    boolean isValid();

}
