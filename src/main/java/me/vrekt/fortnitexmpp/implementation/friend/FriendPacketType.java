package me.vrekt.fortnitexmpp.implementation.friend;

import java.util.List;

public enum FriendPacketType {

    FRIENDSHIP_REQUEST("FRIENDSHIP_REQUEST"),
    FRIENDSHIP_REMOVE("FRIENDSHIP_REMOVE"),
    FRIEND("com.epicgames.friends.core.apiobjects.Friend"),
    FRIEND_REMOVAL("com.epicgames.friends.core.apiobjects.FriendRemoval"),
    UNKNOWN("unknown");

    private static final List<FriendPacketType> TYPES = List.of(values());
    private final String name;

    FriendPacketType(String name) {
        this.name = name;
    }

    public static FriendPacketType from(String type) {
        return TYPES.stream().filter(t -> t.getName().equals(type)).findAny().orElse(UNKNOWN);
    }

    public String getName() {
        return name;
    }

}
