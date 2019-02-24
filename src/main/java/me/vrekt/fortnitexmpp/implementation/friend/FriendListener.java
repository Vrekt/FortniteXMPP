package me.vrekt.fortnitexmpp.implementation.friend;

import org.jivesoftware.smack.packet.Message;

public interface FriendListener {

    /**
     * Invoked when a request is received.
     * Status == "PENDING"
     *
     * @param request the request
     */
    default void onFriendRequestReceived(FriendRequest request) {

    }

    /**
     * Invoked when a request is accepted
     *
     * @param accountId the account id who accepted
     */
    default void onFriendRequestAccepted(String accountId) {

    }

    /**
     * Invoked when a friend request is aborted.
     *
     * @param accountId the account id who aborted
     */
    default void onFriendRequestAborted(String accountId) {

    }

    /**
     * Invoked when a friend request is rejected
     *
     * @param accountId the account id who rejected
     */
    default void onFriendRequestRejected(String accountId) {

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
    default void onFriendMessage(Message message) {

    }

}
