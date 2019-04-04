package me.vrekt.fortnitexmpp.friend;

import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.friend.implementation.FriendListener;

public interface FriendResource extends AutoCloseable {

    /**
     * Adds a friend listener for listening to friend related events
     *
     * @param listener the listener
     */
    void addFriendListener(final FriendListener listener);

    /**
     * Remove a friend listener
     *
     * @param listener the listener
     */
    void removeFriendListener(final FriendListener listener);

    /**
     * Attempts to accept the friend request.
     *
     * @param accountId the account ID.
     */
    boolean acceptOrSendFriendRequest(final String accountId);

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
