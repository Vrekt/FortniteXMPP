package me.vrekt.fortnitexmpp.friend;

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

}
