package me.vrekt.fortnitexmpp.chat.implementation;

public interface IncomingMessageListener {

    /**
     * Invoked when a new message is received.
     *
     * @param message the message
     */
    void messageReceived(Message message);

}
