package me.vrekt.fortnitexmpp.party.implementation.presence;

import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.party.implementation.Party;

import javax.json.Json;

public final class PublicPartyInGamePresence implements PartyPresence {

    private final String status;

    /**
     * Creates a public party, that has the 'join game' option.
     *
     * @param fortniteXMPP      the instance of {@link FortniteXMPP}
     * @param party             the party to use
     * @param platformType      the current platform
     * @param playing           the message to show in the presence.
     *                          for example if this was {@code "Battle Royale Lobby"} then it would show "Battle Royale Lobby - X / X"
     *                          or if this was  {@code "Playing Creative"} then it would show "Playing Creative - X / X"
     * @param sessionId         the session ID of the current session
     * @param sessionKey        the key for the session
     * @param playlist          the current playlist
     * @param playersAlive      how many players are alive
     * @param serverPlayerCount the server player count
     */
    public PublicPartyInGamePresence(final FortniteXMPP fortniteXMPP, final Party party, final FortniteXMPP.PlatformType platformType,
                                     final String playing, final String sessionId, final String sessionKey, final String playlist,
                                     final int playersAlive, final int serverPlayerCount) {
        final var partyJoinInfoData = PresenceUtility.createJoinInfoData(party, fortniteXMPP.accountId(), fortniteXMPP.displayName(), platformType.name());
        final var properties = PresenceUtility.createBasicProperties()
                .add("GamePlaylistName_s", playlist)
                .add("Event_PlayersAlive_s", String.valueOf(playersAlive))
                .add("Event_PartySize_s", String.valueOf(party.members().size()))
                .add("Event_PartyMaxSize_s", String.valueOf(party.configuration().maxMembers()))
                .add(Party.PARTY_DATA_INFO, partyJoinInfoData.build())
                .add("ServerPlayerCount_i", serverPlayerCount)
                .add("GameSessionJoinKey_s", sessionKey);

        this.status = Json.createObjectBuilder()
                .add("Status", playing + " - " + party.members().size() + " / " + party.configuration().maxMembers())
                .add("bIsPlaying", true)
                .add("bIsJoinable", true)
                .add("bHasVoiceSupport", false)
                .add("SessionId", sessionId)
                .add("Properties", properties.build()).build().toString();

        System.err.println("ST: " + status);
    }

    @Override
    public String status() {
        return status;
    }
}
