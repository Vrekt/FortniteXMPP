package me.vrekt.fortnitexmpp.party.implementation.request.member;

import me.vrekt.fortnitexmpp.party.implementation.member.battlepass.BattlePass;
import me.vrekt.fortnitexmpp.party.implementation.member.cosmetic.Backbling;
import me.vrekt.fortnitexmpp.party.implementation.member.cosmetic.Skin;
import me.vrekt.fortnitexmpp.party.implementation.member.input.InputType;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.implementation.request.member.status.PartyMemberStatus;
import me.vrekt.fortnitexmpp.party.type.PartyType;
import me.vrekt.fortnitexmpp.type.PlatformType;

import javax.json.Json;
import javax.json.JsonObject;

public final class PartyMemberData implements PartyRequest {

    private final String payload;

    /**
     * Create a new {@link PartyMemberData}
     * This method initializes a full set of data, used for when you or somebody else joins the party!
     *
     * @param partyId      the ID of the party of who this is going to
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"}
     * @param battlePass   the battle pass to use, or {@code null}
     * @param inputType    the desired input type
     * @param platformType the desired platform type
     * @return a new {@link PartyMemberData}
     */
    public static PartyMemberData create(final String partyId, final String skin, final String backbling,
                                         final BattlePass battlePass, final InputType inputType, final PlatformType platformType) {
        return new PartyMemberData(partyId, skin, backbling, battlePass, inputType, platformType);
    }

    /**
     * Create a new {@link PartyMemberData}
     * This method initializes a full set of data, used for when you or somebody else joins the party!
     *
     * @param partyId      the ID of the party of who this is going to
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"}
     * @param battlePass   the battle pass to use, or {@code null}
     * @param inputType    the desired input type
     * @param platformType the desired platform type
     * @return a new {@link PartyMemberData}
     */
    public static PartyMemberData create(final String partyId, final Skin skin, final Backbling backbling,
                                         final BattlePass battlePass, final InputType inputType, final PlatformType platformType) {
        return new PartyMemberData(partyId, skin.name(), backbling.name(), battlePass, inputType, platformType);
    }

    /**
     * Creates a new {@link PartyMemberData} used to change the skin.
     *
     * @param partyId the ID of the party of who this is going to
     * @param skin    the skin to use
     * @return a new {@link PartyMemberData}
     */
    public static PartyMemberData createToChangeSkin(final String partyId, final String skin) {
        return new PartyMemberData(partyId, skin, null, true, false);
    }

    /**
     * Creates a new {@link PartyMemberData} used to change the backbling
     *
     * @param partyId   the ID of the party of who this is going to
     * @param backbling the backbling to use, or {@code "None"}
     * @return a new {@link PartyMemberData}
     */
    public static PartyMemberData createToChangeBackbling(final String partyId, final String backbling) {
        return new PartyMemberData(partyId, null, backbling, false, true);
    }

    /**
     * Creates a new {@link PartyMemberData} used to change the skin.
     *
     * @param partyId the ID of the party of who this is going to
     * @param skin    the skin to use
     * @return a new {@link PartyMemberData}
     */
    public static PartyMemberData createToChangeSkin(final String partyId, final Skin skin) {
        return new PartyMemberData(partyId, skin.name(), null, true, false);
    }

    /**
     * Creates a new {@link PartyMemberData} used to change the backbling
     *
     * @param partyId   the ID of the party of who this is going to
     * @param backbling the backbling to use, or {@code "None"}
     * @return a new {@link PartyMemberData}
     */
    public static PartyMemberData createToChangeBackbling(final String partyId, final Backbling backbling) {
        return new PartyMemberData(partyId, null, backbling.name(), false, true);
    }

    /**
     * Creates a new {@link PartyMemberData} to change the status, eg: readying up.
     *
     * @param partyId   the ID of the party of who this is going to
     * @param status    the desired status
     * @param inputType the desired input type
     * @return a new {@link PartyMemberData}
     */
    public static PartyMemberData createToSetStatus(final String partyId, final PartyMemberStatus status, final InputType inputType) {
        return new PartyMemberData(partyId, status, inputType);
    }

    /**
     * Creates a new {@link PartyMemberData} used to notify the party your content is preloaded.
     *
     * @param partyId the ID of the party of who this is going to
     * @return a new {@link PartyMemberData}
     */
    public static PartyMemberData createToPreloadContent(final String partyId) {
        return new PartyMemberData(partyId);
    }

    /**
     * Initializes this instance
     *
     * @param partyId      the ID of the party of who this is going to
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"}
     * @param battlePass   the battle pass to use, or {@code null}
     * @param inputType    the desired input type
     * @param platformType the desired platform type
     */
    private PartyMemberData(final String partyId, final String skin, final String backbling,
                            final BattlePass battlePass, final InputType inputType, final PlatformType platformType) {
        // initialize battlepass info.
        final var hasPurchased = battlePass != null && battlePass.hasPurchased();
        final var friendBoostXp = battlePass == null ? 0 : battlePass.getFriendBoostXp();
        final var selfBoostXp = battlePass == null ? 0 : battlePass.getSelfBoostXp();
        final var level = battlePass == null ? 1 : battlePass.getPassLevel();

        // build the hero definition and then the emote definition
        final var heroData = buildHeroDefinition(skin);
        final var frontendData = Json.createObjectBuilder()
                .add("emoteItemDef", "None")
                .add("emoteItemDefEncryptionKey", "")
                .add("emoteSection", -1);

        // banner and cosmetic loadout.
        final var cosmeticLoadout = buildCosmeticLoadout(skin, backbling, true, true);
        final var bannerData = Json.createObjectBuilder();
        // TODO: Add method for changing banners? Keep default for now.
        bannerData.add("bannerIconId", "standardbanner15");
        bannerData.add("bannerColorId", "defaultcolor17");
        bannerData.add("seasonLevel", 1);

        // battle pass information
        final var battlePassData = Json.createObjectBuilder();
        battlePassData.add("bHasPurchasedPass", hasPurchased);
        battlePassData.add("passLevel", level);
        battlePassData.add("selfBoostXp", selfBoostXp);
        battlePassData.add("friendBoostXp", friendBoostXp);

        // build it!
        final var payload = Json.createObjectBuilder();
        final var attributes = Json.createObjectBuilder()
                .add("Location_s", "PreLobby")
                .add("CampaignHero_j", Json.createObjectBuilder()
                        .add("CampaignHero", heroData).build())
                .add("MatchmakingLevel_U", "0")
                .add("ZoneInstanceId_S", "")
                .add("HomeBaseVersion_U", "1")
                .add("HasPreloadedAthena_b", false)
                .add("FrontendEmote_j", Json.createObjectBuilder()
                        .add("FrontendEmote", frontendData.build()).build())
                .add("NumAthenaPlayersLeft_U", "0")
                .add("UtcTimeStartedMatchAthena_s", "0001-01-01T00:00:00.000Z")
                .add("GameReadiness_s", "NotReady")
                .add("HiddenMatchmakingDelayMax_U", "0")
                .add("ReadyInputType_s", "Count")
                .add("CurrentInputType_s", inputType.getName())
                .add("AssistedChallengeInfo_j", Json.createObjectBuilder()
                        .add("AssistedChallengeInfo", Json.createObjectBuilder()
                                .add("questItemDef", "None")
                                .add("objectivesCompleted", 0).build()).build())
                .add("MemberSquadAssignmentRequest_j", Json.createObjectBuilder()
                        .add("MemberSquadAssignmentRequest", Json.createObjectBuilder()
                                .add("startingAbsoluteIdx", -1)
                                .add("targetAbsoluteIdx", -1)
                                .add("swapTargetMemberId", "INVALID").build()).build())
                .add("AthenaCosmeticLoadout_j", Json.createObjectBuilder()
                        .add("AthenaCosmeticLoadout", cosmeticLoadout).build())
                .add("AthenaBannerInfo_j", Json.createObjectBuilder()
                        .add("AthenaBannerInfo", bannerData.build()).build())
                .add("BattlePassInfo_j", Json.createObjectBuilder()
                        .add("BattlePassInfo", battlePassData.build()).build())
                .add("Platform_j", Json.createObjectBuilder()
                        .add("Platform", Json.createObjectBuilder()
                                .add("platformStr", platformType.name()).build()).build())
                .add("PlatformUniqueId_s", "INVALID")
                .add("PlatformSessionId_s", "")
                .add("CrossplayPreference_s", "OptedIn");

        payload.add("Rev", RequestBuilder.getRevisionFor(PartyType.PARTY_MEMBER_DATA));
        payload.add("Attrs", attributes.build());
        this.payload = RequestBuilder.buildRequestDoublePayload(partyId, payload.build(), PartyType.PARTY_MEMBER_DATA).toString();
    }

    /**
     * Initialize for a skin or backbling change.
     *
     * @param partyId          the ID of the party.
     * @param skin             the skin
     * @param backbling        the backbling
     * @param includeCharacter {@code true} if the skin definition should be included
     * @param includeBackbling {@code true} if the backpack definition should be included
     */
    private PartyMemberData(final String partyId, final String skin, final String backbling, final boolean includeCharacter, final boolean includeBackbling) {
        final var cosmeticLoadout = buildCosmeticLoadout(skin, backbling, includeCharacter, includeBackbling);
        final var payload = Json.createObjectBuilder();
        final var attributes = Json.createObjectBuilder();
        if (includeCharacter) {
            final var heroData = buildHeroDefinition(skin);
            attributes.add("CampaignHero_j", Json.createObjectBuilder()
                    .add("CampaignHero", heroData).build())
                    .add("AthenaCosmeticLoadout_j", Json.createObjectBuilder()
                            .add("AthenaCosmeticLoadout", cosmeticLoadout).build());
        } else if (includeBackbling) {
            attributes.add("CampaignHero_j", Json.createObjectBuilder()
                    .build())
                    .add("AthenaCosmeticLoadout_j", Json.createObjectBuilder()
                            .add("AthenaCosmeticLoadout", cosmeticLoadout).build());
        }

        payload.add("Rev", RequestBuilder.getRevisionFor(PartyType.PARTY_MEMBER_DATA));
        payload.add("Attrs", attributes.build());
        this.payload = RequestBuilder.buildRequestDoublePayload(partyId, payload.build(), PartyType.PARTY_MEMBER_DATA).toString();
    }

    /**
     * Initialize to change the read status.
     *
     * @param partyId   the ID of the party.
     * @param status    the status
     * @param inputType the desired input type
     */
    private PartyMemberData(final String partyId, final PartyMemberStatus status, final InputType inputType) {
        final var payload = Json.createObjectBuilder();
        final var attributes = Json.createObjectBuilder();

        payload.add("Rev", RequestBuilder.getRevisionFor(PartyType.PARTY_MEMBER_DATA));
        attributes.add("GameReadiness_s", status.getName());
        if (status == PartyMemberStatus.READY || status == PartyMemberStatus.NOT_READY) {
            attributes.add("ReadyInputType_s", status == PartyMemberStatus.READY ? inputType.getName() : "Count");
        }

        payload.add("Attrs", attributes.build());
        this.payload = RequestBuilder.buildRequestDoublePayload(partyId, payload.build(), PartyType.PARTY_MEMBER_DATA).toString();
    }

    /**
     * Initialize to change the preloaded status
     *
     * @param partyId the ID of the party.
     */
    private PartyMemberData(final String partyId) {
        final var payload = Json.createObjectBuilder();
        final var attributes = Json.createObjectBuilder();

        payload.add("Rev", RequestBuilder.getRevisionFor(PartyType.PARTY_MEMBER_DATA));
        attributes.add("HasPreloadedAthena_b", true);
        payload.add("Attrs", attributes.build());
        this.payload = RequestBuilder.buildRequestDoublePayload(partyId, payload.build(), PartyType.PARTY_MEMBER_DATA).toString();
    }

    /**
     * Builds a character definition from the provided values.
     *
     * @param skin             the skin
     * @param backbling        the backbling
     * @param includeBackbling {@code} true if a backpack definition should be included.
     * @return a new {@link JsonObject}
     */
    private JsonObject buildCosmeticLoadout(final String skin, final String backbling, final boolean includeCharacter, final boolean includeBackbling) {
        final var cosmeticLoadout = Json.createObjectBuilder();
        if (includeCharacter) {
            cosmeticLoadout.add("characterDefinition", "AthenaCharacterItemDefinition'/Game/Athena/Items/Cosmetics/Characters/" + skin + "." + skin + "'");
            cosmeticLoadout.add("characterDefinitionEncryptionKey", "");
        }

        if (includeBackbling) {
            if (backbling.equalsIgnoreCase("none")) {
                cosmeticLoadout.add("backpackDefinition", "None");
            } else {
                cosmeticLoadout.add("backpackDefinition", "AthenaBackpackItemDefinition'/Game/Athena/Items/Cosmetics/Backpacks/" + backbling + "." + backbling + "'");
            }
            cosmeticLoadout.add("backpackDefinitionEncryptionKey", "");
        }
        return cosmeticLoadout.build();
    }

    /**
     * Builds a hero definition based on skin
     *
     * @param skin the skin
     * @return a new {@link JsonObject}
     */
    private JsonObject buildHeroDefinition(final String skin) {
        final var heroData = Json.createObjectBuilder();
        String heroName = skin.replace("CID", "HID");
        heroData.add("heroItemInstanceId", "");
        heroData.add("heroType", "FortHeroType'/Game/Athena/Heroes/" + heroName + "." + heroName + "'");
        return heroData.build();
    }

    @Override
    public String payload() {
        return payload;
    }
}
