package me.vrekt.fortnitexmpp.party.implementation.presence;

import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.type.PlatformType;

import javax.json.Json;

/**
 * https://github.com/Vrekt/FortniteXMPP/wiki/Presence-Payload
 */
public final class PublicPartyPresence implements PartyPresence {

    private final String status;

    /**
     * Creates a public party presence
     *
     * @param fortniteXMPP the instance of {@link FortniteXMPP}
     * @param party        the party to use
     * @param platformType the platform type to use
     */
    public PublicPartyPresence(final FortniteXMPP fortniteXMPP, final Party party, final PlatformType platformType) {
        final var partyJoinInfoData = PresenceUtility.createJoinInfoData(party, fortniteXMPP.accountId(), fortniteXMPP.displayName(), platformType.name());
        final var basicProperties = PresenceUtility.createBasicProperties();

        this.status = Json.createObjectBuilder()
                .add("Status", "Battle Royale Lobby - " + party.members().size() + " / " + party.configuration().maxMembers())
                .add("bIsPlaying", false)
                .add("bIsJoinable", true)
                .add("bHasVoiceSupport", false)
                .add("SessionId", "")
                .add("Properties", Json.createObjectBuilder()
                        .add(Party.PARTY_DATA_INFO, partyJoinInfoData.build())
                        .addAll(basicProperties).build()).build().toString();
    }


    @Override
    public String status() {
        return status;
    }
}
