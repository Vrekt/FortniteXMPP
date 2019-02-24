package me.vrekt.fortnitexmpp.implementation.party;

public enum InputType {

    KEYBOARD_AND_MOUSE("MouseAndKeyboard"), TOUCH("Touch"), GAMEPAD("Gamepad");

    private final String name;

    InputType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
