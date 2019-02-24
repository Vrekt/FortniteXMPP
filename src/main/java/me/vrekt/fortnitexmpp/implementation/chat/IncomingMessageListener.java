package me.vrekt.fortnitexmpp.implementation.chat;

import me.vrekt.fortnitexmpp.implementation.chat.message.Message;

public interface IncomingMessageListener {

    /**
     * Invoked when a message is received.
     *
     * @param message the message
     */
    void onMessageReceived(Message message);

}