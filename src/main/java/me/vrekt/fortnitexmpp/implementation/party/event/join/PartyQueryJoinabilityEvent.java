package me.vrekt.fortnitexmpp.implementation.party.event.join;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;

public final class PartyQueryJoinabilityEvent {

    private final Party party;
    private final Jid from;
    private int crossplayPreference;

    public PartyQueryJoinabilityEvent(Party party, JsonObject payload, Jid from) {
        this.party = party;
        this.from = from;

        JsonUtility.getObject("joinData", payload).
                ifPresent(joinData -> JsonUtility.getObject("Attrs", joinData).
                        ifPresent(attributes -> JsonUtility.getInt("CrossplayPreference_i", attributes).
                                ifPresent(crossplayPreference -> this.crossplayPreference = crossplayPreference)));
    }

    public Party getParty() {
        return party;
    }

    /**
     * 1 = OPTED IN
     * OTHER VALUES UNKNOWN
     *
     * @return the crossplay preference of the client
     */
    public int getCrossplayPreference() {
        return crossplayPreference;
    }

    public Jid getFrom() {
        return from;
    }
}
