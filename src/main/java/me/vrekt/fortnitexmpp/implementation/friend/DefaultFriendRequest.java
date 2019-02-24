package me.vrekt.fortnitexmpp.implementation.friend;

import me.vrekt.fortnitexmpp.utility.JsonUtility;

import javax.json.JsonObject;
import java.util.function.Consumer;

public final class DefaultFriendRequest implements FriendRequest {

    private final String accountId;
    private final Consumer<String> acceptConsumer;

    DefaultFriendRequest(JsonObject payload, Consumer<String> acceptConsumer) {
        accountId = JsonUtility.getString("from", payload).orElse("");
        this.acceptConsumer = acceptConsumer;
    }

    @Override
    public String getFrom() {
        return accountId;
    }

    @Override
    public void accept() {
        acceptConsumer.accept(accountId);
    }
}
