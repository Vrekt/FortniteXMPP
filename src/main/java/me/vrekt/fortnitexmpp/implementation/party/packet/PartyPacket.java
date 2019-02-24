package me.vrekt.fortnitexmpp.implementation.party.packet;

import org.jxmpp.jid.Jid;

public interface PartyPacket {

    /**
     * The JSON payload as a string to send.
     *
     * @return the payload
     */
    String getPayload();

    /**
     * Who the payload should be sent to
     *
     * @return the JID
     */
    Jid getTo();

}
