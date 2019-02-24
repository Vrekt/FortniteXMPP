package me.vrekt.fortnitexmpp.implementation.service.friend;

import me.vrekt.fortnitexmpp.implementation.friend.FriendListener;
import me.vrekt.fortnitexmpp.implementation.service.Service;

public interface FriendService extends Service {

    /**
     * Adds a friend listener for listening to friend related events
     *
     * @param listener the listener
     */
    void addFriendListener(FriendListener listener);

    /**
     * Remove a friend listener
     *
     * @param listener the listener
     */
    void removeFriendListener(FriendListener listener);

}
