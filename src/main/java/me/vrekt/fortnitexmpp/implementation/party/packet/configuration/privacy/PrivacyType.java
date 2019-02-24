package me.vrekt.fortnitexmpp.implementation.party.packet.configuration.privacy;

public enum PrivacyType {

    PUBLIC("Public"), FRIENDS("FriendsOnly"), PRIVATE("Private");

    private final String name;

    PrivacyType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static PrivacyType of(String name) {
        for (PrivacyType type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
