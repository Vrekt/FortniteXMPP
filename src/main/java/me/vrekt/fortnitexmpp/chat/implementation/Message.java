package me.vrekt.fortnitexmpp.chat.implementation;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jxmpp.jid.EntityBareJid;

public final class Message {

    private final EntityBareJid from;
    private final String message;

    private final Chat chat;

    public Message(final EntityBareJid from, final String message, final Chat chat) {
        this.from = from;
        this.message = message;
        this.chat = chat;
    }

    /**
     * @return who the message was from
     */
    public EntityBareJid getFrom() {
        return from;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the chat
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * Attempt to reply
     *
     * @param message the message to reply with
     * @return false if replying failed.
     */
    public boolean reply(final String message) {
        try {
            chat.send(message);
        } catch (final SmackException.NotConnectedException | InterruptedException exception) {
            return false;
        }
        return true;
    }

}
