package me.vrekt.fortnitexmpp.party.implementation.request.member;

import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.party.implementation.member.battlepass.BattlePass;
import me.vrekt.fortnitexmpp.party.implementation.member.cosmetic.Backbling;
import me.vrekt.fortnitexmpp.party.implementation.member.cosmetic.Skin;
import me.vrekt.fortnitexmpp.party.implementation.member.input.InputType;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;
import javax.json.JsonObject;

public final class PartyMemberData implements PartyRequest {

    private final JsonObject object;
    private String payload;

    /**
     * Create a new {@link PartyMemberData} set for yourself.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"} for no backbling.
     * @param inputType    the input type to use
     * @param platformType the desired platform
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forMyself(final String skin, final String backbling, final InputType inputType, final FortniteXMPP.PlatformType platformType) {
        return new PartyMemberData(skin, backbling, inputType, null, platformType);
    }

    /**
     * Create a new {@link PartyMemberData} set for yourself.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"} for no backbling.
     * @param inputType    the input type to use
     * @param platformType the desired platform
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forMyself(final Skin skin, final String backbling, final InputType inputType, final FortniteXMPP.PlatformType platformType) {
        return new PartyMemberData(skin.name(), backbling, inputType, null, platformType);
    }

    /**
     * Create a new {@link PartyMemberData} set for yourself.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"} for no backbling.
     * @param inputType    the input type to use
     * @param platformType the desired platform
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forMyself(final Skin skin, final Backbling backbling, final InputType inputType, final FortniteXMPP.PlatformType platformType) {
        return new PartyMemberData(skin.name(), backbling.getName(), inputType, null, platformType);
    }

    /**
     * Create a new {@link PartyMemberData} set for yourself.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"} for no backbling.
     * @param battlePass   the battle pass to use.
     * @param inputType    the input type to use
     * @param platformType the desired platform
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forMyself(final String skin, final String backbling, final BattlePass battlePass, final InputType inputType, final FortniteXMPP.PlatformType platformType) {
        return new PartyMemberData(skin, backbling, inputType, battlePass, platformType);
    }

    /**
     * Create a new {@link PartyMemberData} set for yourself.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"} for no backbling.
     * @param battlePass   the battle pass to use.
     * @param inputType    the input type to use
     * @param platformType the desired platform
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forMyself(final String skin, final Backbling backbling, final BattlePass battlePass, final InputType inputType, final FortniteXMPP.PlatformType platformType) {
        return new PartyMemberData(skin, backbling.getName(), inputType, battlePass, platformType);
    }

    /**
     * Create a new {@link PartyMemberData} set for yourself.
     * This method is used for when you dealing with multiple parties and want to build your own set
     * of data when required instead of storing it.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"} for no backbling.
     * @param inputType    the input type to use
     * @param platformType the desired platform
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forMyselfOnce(final String skin, final String backbling, final InputType inputType, final FortniteXMPP.PlatformType platformType,
                                                final String partyId) {
        final var data = new PartyMemberData(skin, backbling, inputType, null, platformType);
        data.setPartyIdAndBuild(partyId);
        return data;
    }

    /**
     * Create a new {@link PartyMemberData} set for yourself.
     * This method is used for when you dealing with multiple parties and want to build your own set
     * of data when required instead of storing it.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"} for no backbling.
     * @param inputType    the input type to use
     * @param platformType the desired platform
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forMyselfOnce(final Skin skin, final String backbling, final InputType inputType, final FortniteXMPP.PlatformType platformType,
                                                final String partyId) {
        final var data = new PartyMemberData(skin.name(), backbling, inputType, null, platformType);
        data.setPartyIdAndBuild(partyId);
        return data;
    }

    /**
     * Create a new {@link PartyMemberData} set for yourself.
     * This method is used for when you dealing with multiple parties and want to build your own set
     * of data when required instead of storing it.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"} for no backbling.
     * @param inputType    the input type to use
     * @param platformType the desired platform
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forMyselfOnce(final Skin skin, final Backbling backbling, final InputType inputType, final FortniteXMPP.PlatformType platformType,
                                                final String partyId) {
        final var data = new PartyMemberData(skin.name(), backbling.getName(), inputType, null, platformType);
        data.setPartyIdAndBuild(partyId);
        return data;
    }

    /**
     * Create a new {@link PartyMemberData} set for yourself.
     * This method is used for when you dealing with multiple parties and want to build your own set
     * of data when required instead of storing it.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"} for no backbling.
     * @param battlePass   the battle pass to use.
     * @param inputType    the input type to use
     * @param platformType the desired platform
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forMyselfOnce(final String skin, final String backbling, final BattlePass battlePass, final InputType inputType,
                                                final FortniteXMPP.PlatformType platformType, final String partyId) {
        final var data = new PartyMemberData(skin, backbling, inputType, battlePass, platformType);
        data.setPartyIdAndBuild(partyId);
        return data;
    }

    /**
     * Create a new {@link PartyMemberData} set for yourself.
     * This method is used for when you dealing with multiple parties and want to build your own set
     * of data when required instead of storing it.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"} for no backbling.
     * @param battlePass   the battle pass to use.
     * @param inputType    the input type to use
     * @param platformType the desired platform
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forMyselfOnce(final String skin, final Backbling backbling, final BattlePass battlePass, final InputType inputType,
                                                final FortniteXMPP.PlatformType platformType, final String partyId) {
        final var data = new PartyMemberData(skin, backbling.getName(), inputType, battlePass, platformType);
        data.setPartyIdAndBuild(partyId);
        return data;
    }


    /**
     * Create a new {@link PartyMemberData} with the provided {@code skin}
     *
     * @param skin the skin to use
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forNewSkin(final String skin) {
        return new PartyMemberData(skin, "None", true, false);
    }

    /**
     * Create a new {@link PartyMemberData} with the provided {@code backbling}
     *
     * @param backbling the backbling to use, this can be {@code "None"}
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forNewBackbling(final String backbling) {
        return new PartyMemberData("None", backbling, false, true);
    }

    /**
     * Create a new {@link PartyMemberData} with the provided {@code isReady} and {@code readyInputType}
     *
     * @param isReady        {@code} true if this character is ready.
     * @param readyInputType the input type to use
     * @return a new {@link PartyMemberData} instance
     */
    public static PartyMemberData forReadyStatus(final boolean isReady, final InputType readyInputType) {
        return new PartyMemberData(isReady, readyInputType);
    }

    /**
     * Create a new {@link PartyMemberData} with the provided {@code hasPreloaded}
     *
     * @param hasPreloaded {@code true} if this character has preloaded their in-game content.
     * @return a new {@link PartyMemberData} instance.
     */
    public static PartyMemberData forPreloaded(final boolean hasPreloaded) {
        return new PartyMemberData(hasPreloaded);
    }

    /**
     * Initializes a full set of sendable data.
     *
     * @param skin      the skin to use, must always be set.
     * @param backbling the backbling to use, or {@code "None"} for no backbling.
     * @param inputType the desired input type.
     */
    private PartyMemberData(final String skin, final String backbling, final InputType inputType, final BattlePass battlePass, final FortniteXMPP.PlatformType platformType) {
        // initialize battlepass info.
        final var hasPurchased = battlePass != null && battlePass.hasPurchased();
        final var friendBoostXp = battlePass == null ? 0 : battlePass.getFriendBoostXp();
        final var selfBoostXp = battlePass == null ? 0 : battlePass.getSelfBoostXp();
        final var level = battlePass == null ? 1 : battlePass.getPassLevel();

        // hero and emote.
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
        this.object = payload.build();
    }

    /**
     * Initialize for a skin or backbling change.
     *
     * @param skin             the skin
     * @param backbling        the backbling
     * @param includeCharacter {@code true} if the skin definition should be included
     * @param includeBackbling {@code true} if the backpack definition should be included
     */
    private PartyMemberData(final String skin, final String backbling, final boolean includeCharacter, final boolean includeBackbling) {
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
        this.object = payload.build();
    }

    /**
     * Initialize to change the read status.
     *
     * @param isReady   {@code true} if this character is ready
     * @param inputType the desired input type
     */
    private PartyMemberData(final boolean isReady, final InputType inputType) {
        final var payload = Json.createObjectBuilder();
        final var attributes = Json.createObjectBuilder();

        payload.add("Rev", RequestBuilder.getRevisionFor(PartyType.PARTY_MEMBER_DATA));
        attributes.add("GameReadiness_s", isReady ? "Ready" : "NotReady");
        attributes.add("ReadyInputType_s", isReady ? inputType.getName() : "Count");
        payload.add("Attrs", attributes.build());
        this.object = payload.build();
    }

    /**
     * Initialize to change the preloaded status
     *
     * @param hasPreloaded {@code true} if this character has preloaded their content
     */
    private PartyMemberData(final boolean hasPreloaded) {
        final var payload = Json.createObjectBuilder();
        final var attributes = Json.createObjectBuilder();

        payload.add("Rev", RequestBuilder.getRevisionFor(PartyType.PARTY_MEMBER_DATA));
        attributes.add("HasPreloadedAthena_b", hasPreloaded);
        payload.add("Attrs", attributes.build());
        this.object = payload.build();
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
            cosmeticLoadout.add("cosmeticVariants", Json.createArrayBuilder().build());
        }

        if (includeBackbling) {
            if (backbling.equalsIgnoreCase("none")) {
                cosmeticLoadout.add("backpackDefinition", "None");
            } else {
                cosmeticLoadout.add("backpackDefinition", "AthenaBackpackItemDefinition'/Game/Athena/Items/Cosmetics/Backpacks/" + backbling + "." + backbling + "'");
            }
            cosmeticLoadout.add("backpackDefinitionEncryptionKey", "");
            cosmeticLoadout.add("cosmeticVariants", Json.createArrayBuilder().build());
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

    /**
     * Set the party ID and then build the JSON payload.
     *
     * @param partyId the ID of the party
     */
    public void setPartyIdAndBuild(final String partyId) {
        this.payload = RequestBuilder.buildRequestDoublePayload(partyId, object, PartyType.PARTY_MEMBER_DATA).toString();
    }

    @Override
    public String payload() {
        return payload;
    }
}
