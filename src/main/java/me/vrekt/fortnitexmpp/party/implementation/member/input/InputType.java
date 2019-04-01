package me.vrekt.fortnitexmpp.party.implementation.member.input;

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
