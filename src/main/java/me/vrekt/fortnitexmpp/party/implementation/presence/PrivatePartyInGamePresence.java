package me.vrekt.fortnitexmpp.party.implementation.presence;

import me.vrekt.fortnitexmpp.party.implementation.Party;

import javax.json.Json;

/**
 * https://github.com/Vrekt/FortniteXMPP/wiki/Presence-Payload
 */

public final class PrivatePartyInGamePresence implements PartyPresence {

    private final String status;

    /**
     * Creates a private party in game presence.
     *
     * @param party             the current party
     * @param playing           what is currently being played,
     *                          if {@code "Battle Royale Lobby"} it will show "Battle Royale Lobby - X / X"
     *                          if {@code "Playing Creative"} it will show "Playing Creative - X / X"
     * @param sessionId         the session ID
     * @param playlist          the current playlist
     * @param playersAlive      how many players are alive
     * @param serverPlayerCount how many players are in the server
     */
    public PrivatePartyInGamePresence(final Party party, final String playing, final String sessionId,
                                      final String playlist, final int playersAlive, final int serverPlayerCount) {
        final var properties = PresenceUtility.createBasicProperties()
                .add("GamePlaylistName_s", playlist)
                .add("Event_PlayersAlive_s", String.valueOf(playersAlive))
                .add("Event_PartySize_s", String.valueOf(party.members().size()))
                .add("Event_PartyMaxSize_s", String.valueOf(party.configuration().maxMembers()))
                .add("ServerPlayerCount_i", serverPlayerCount);

        this.status = Json.createObjectBuilder()
                .add("Status", playing + " - " + party.members().size() + " / " + party.configuration().maxMembers())
                .add("bIsPlaying", true)
                .add("bIsJoinable", true)
                .add("bHasVoiceSupport", false)
                .add("SessionId", sessionId)
                .add("Properties", properties.build()).build().toString();
    }

    @Override
    public String status() {
        return status;
    }
}
