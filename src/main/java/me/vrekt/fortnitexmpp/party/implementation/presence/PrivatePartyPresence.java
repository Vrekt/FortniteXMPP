package me.vrekt.fortnitexmpp.party.implementation.presence;

import me.vrekt.fortnitexmpp.party.implementation.Party;

import javax.json.Json;

public final class PrivatePartyPresence implements PartyPresence {

    private final String status;

    /**
     * Creates a private party presence.
     *
     * @param party the party to use
     */
    public PrivatePartyPresence(final Party party) {
        final var basicProperties = PresenceUtility.createBasicProperties();

        this.status = Json.createObjectBuilder()
                .add("Status", "Battle Royale Lobby - " + party.members().size() + " / " + party.configuration().maxMembers())
                .add("bIsPlaying", false)
                .add("bIsJoinable", true)
                .add("bHasVoiceSupport", false)
                .add("SessionId", "")
                .add("Properties", basicProperties.build()).build().toString();
    }

    @Override
    public String status() {
        return null;
    }
}
