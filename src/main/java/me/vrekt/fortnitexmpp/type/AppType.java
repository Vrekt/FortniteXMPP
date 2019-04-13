package me.vrekt.fortnitexmpp.type;

public enum AppType {

    FORTNITE("Fortnite"), LAUNCHER("launcher");

    private final String name;

    AppType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
