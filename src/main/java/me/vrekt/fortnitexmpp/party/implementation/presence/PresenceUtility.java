package me.vrekt.fortnitexmpp.party.implementation.presence;

import me.vrekt.fortnitexmpp.party.implementation.DefaultParty;
import me.vrekt.fortnitexmpp.party.implementation.Party;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public final class PresenceUtility {

    public static JsonObjectBuilder createJoinInfoData(final Party party, final String accountId, final String displayName, final String platform) {
        return Json.createObjectBuilder()
                .add("sourceId", accountId)
                .add("sourceDisplayName", displayName)
                .add("sourcePlatform", platform)
                .add("partyId", party.partyId())
                .add("partyTypeId", Party.TYPE_ID)
                .add("key", party.accessKey())
                .add("appId", "Fortnite")
                .add("buildId", String.valueOf(DefaultParty.buildId))
                .add("partyFlags", 6)
                .add("notAcceptingReason", 0);
    }

    public static JsonObjectBuilder createBasicProperties() {
        return Json.createObjectBuilder()
                .add("FortBasicInfo_j", Json.createObjectBuilder()
                        .add("homeBaseRating", 1).build())
                .add("FortLFG_I", "0")
                .add("FortPartySize_i", 1)
                .add("FortSubGame_i", 1)
                .add("InUnjoinableMatch_b", true);
    }

}
