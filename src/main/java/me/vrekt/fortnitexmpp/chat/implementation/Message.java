package me.vrekt.fortnitexmpp.chat.implementation;

import org.jxmpp.jid.Jid;

public final class Message {

    private final Jid from;
    private final String message;

    public Message(final Jid from, final String message) {
        this.from = from;
        this.message = message;
    }

    /**
     * @return who the message was from
     */
    public Jid from() {
        return from;
    }

    /**
     * @return the message
     */
    public String message() {
        return message;
    }

}
