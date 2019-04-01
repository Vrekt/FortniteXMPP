package me.vrekt.fortnitexmpp.friend.implementation;

import org.jivesoftware.smack.packet.Message;

public interface FriendListener {

    /**
     * Invoked when a request is received.
     *
     * @param accountId the account ID who sent it.
     */
    default void onFriendRequestReceived(final String accountId) {

    }

    /**
     * Invoked when a request is accepted
     *
     * @param accountId the account id who accepted
     */
    default void onFriendRequestAccepted(final String accountId) {

    }

    /**
     * Invoked when a friend request is aborted.
     *
     * @param accountId the account id who aborted
     */
    default void onFriendRequestAborted(final String accountId) {

    }

    /**
     * Invoked when a friend request is rejected
     *
     * @param accountId the account id who rejected
     */
    default void onFriendRequestRejected(final String accountId) {

    }

    /**
     * Invoked when somebody removes this account from their friends list
     *
     * @param accountId the account id.
     */
    default void onFriendRequestDeleted(String accountId) {

    }

    /**
     * Invoked when a friend message is received.
     *
     * @param message the message
     */
    default void onXMPPFriendMessage(Message message) {

    }

}
