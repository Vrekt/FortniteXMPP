package me.vrekt.fortnitexmpp.implementation.party.event.data;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.packet.configuration.PartyConfiguration;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class PartyConfigurationUpdatedEvent {

    private final Party party;
    private final Jid from;
    private PartyConfiguration configuration;

    public PartyConfigurationUpdatedEvent(Party party, JsonObject payload, Jid from) {
        this.party = party;
        this.from = from;

        var presencePermissions = new AtomicLong();
        var invitePermissions = new AtomicInteger();
        var partyFlags = new AtomicInteger();
        var notAcceptingMembersReason = new AtomicInteger();
        var maxMembers = new AtomicInteger();

        JsonUtility.getLong("presencePermissions", payload).ifPresent(presencePermissions::set);
        JsonUtility.getInt("invitePermissions", payload).ifPresent(invitePermissions::set);
        JsonUtility.getInt("partyFlags", payload).ifPresent(partyFlags::set);
        JsonUtility.getInt("notAcceptingMembersReason", payload).ifPresent(notAcceptingMembersReason::set);
        JsonUtility.getInt("maxMembers", payload).ifPresent(maxMembers::set);

        configuration = new PartyConfiguration(invitePermissions.get(), partyFlags.get(), notAcceptingMembersReason.get(), maxMembers.get(), presencePermissions.get());
    }

    public Party getParty() {
        return party;
    }

    public Jid getFrom() {
        return from;
    }

    public PartyConfiguration getConfiguration() {
        return configuration;
    }
}
