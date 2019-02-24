package me.vrekt.fortnitexmpp.implementation.friend;

import javax.json.JsonObject;
import java.util.function.Consumer;

public interface FriendRequest {

    /**
     * Get who the friend request was from.
     *
     * @return an account ID.
     */
    String getFrom();

    /**
     * Attempts to accept the friend request.
     */
    void accept();

    static FriendRequest newInstance(JsonObject payload, Consumer<String> acceptConsumer) {
        return new DefaultFriendRequest(payload, acceptConsumer);
    }

    enum Status {
        ACCEPTED, PENDING, ABORTED, REJECTED, DELETED
    }

}
