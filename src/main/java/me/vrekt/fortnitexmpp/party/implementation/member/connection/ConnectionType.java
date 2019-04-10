package me.vrekt.fortnitexmpp.party.implementation.member.connection;

public enum ConnectionType {

    GAME("game");

    private final String name;

    ConnectionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ConnectionType getType(final String type) {
        if (!type.equals("game")) {
            System.err.println("Found unknown connection type, please report this.");
        }
        return GAME;
    }

}
