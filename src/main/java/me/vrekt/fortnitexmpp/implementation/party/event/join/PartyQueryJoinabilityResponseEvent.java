package me.vrekt.fortnitexmpp.implementation.party.event.join;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;

/**
 * An event for when a {@link me.vrekt.fortnitexmpp.implementation.party.packet.authentication.server.PacketPartyQueryJoinabilityResponse} packet is received.
 */
public final class PartyQueryJoinabilityResponseEvent {

    private final Party party;
    private final Jid from;

    private boolean isJoinable;
    private int rejectionType;
    private String resultParam;

    public PartyQueryJoinabilityResponseEvent(Party party, JsonObject payload, Jid from) {
        this.party = party;
        this.from = from;

        JsonUtility.getBoolean("isJoinable", payload).ifPresent(isJoinable -> this.isJoinable = isJoinable);
        JsonUtility.getInt("rejectionType", payload).ifPresent(rejectionType -> this.rejectionType = rejectionType);
        JsonUtility.getString("resultParam", payload).ifPresent(resultParam -> this.resultParam = resultParam);
    }

    public Party getParty() {
        return party;
    }

    public Jid getFrom() {
        return from;
    }

    public boolean isJoinable() {
        return isJoinable;
    }

    public int getRejectionType() {
        return rejectionType;
    }

    public String getResultParam() {
        return resultParam;
    }
}
