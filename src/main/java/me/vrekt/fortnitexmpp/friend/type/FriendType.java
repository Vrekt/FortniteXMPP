package me.vrekt.fortnitexmpp.friend.type;

import java.util.List;

public enum FriendType {

    FRIENDSHIP_REQUEST("FRIENDSHIP_REQUEST"),
    FRIENDSHIP_REMOVE("FRIENDSHIP_REMOVE"),
    FRIEND_REMOVAL("com.epicgames.friends.core.apiobjects.FriendRemoval"),
    FRIEND("com.epicgames.friends.core.apiobjects.Friend");

    private static final List<FriendType> TYPES = List.of(values());
    private final String name;

    FriendType(String name) {
        this.name = name;
    }

    public static FriendType typeOf(String otherType) {
        if(otherType == null) return null;
        return TYPES.stream().filter(t -> t.getName().equals(otherType)).findAny().orElse(null);
    }

    public String getName() {
        return name;
    }

}
