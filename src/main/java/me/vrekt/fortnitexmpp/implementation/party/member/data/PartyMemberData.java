package me.vrekt.fortnitexmpp.implementation.party.member.data;

import me.vrekt.fortnitexmpp.implementation.PlatformType;
import me.vrekt.fortnitexmpp.implementation.party.InputType;
import me.vrekt.fortnitexmpp.implementation.party.member.battlepass.BattlePass;
import me.vrekt.fortnitexmpp.implementation.party.member.cosmetic.Backbling;
import me.vrekt.fortnitexmpp.implementation.party.member.cosmetic.Skin;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;

import javax.json.Json;
import javax.json.JsonObject;

public final class PartyMemberData {

    private JsonObject payload;

    private PartyMemberData(Builder builder) {
        if (builder.type == MemberDataBuildType.MEMBER) {
            initializeMember(builder);
        } else if (builder.type == MemberDataBuildType.OTHER_MEMBER) {
            initializeOtherMember(builder);
        } else if (builder.type == MemberDataBuildType.SKIN) {
            initializeSkinChange(builder);
        } else if (builder.type == MemberDataBuildType.BACKBLING) {
            initializeBackblingChange(builder);
        } else if (builder.type == MemberDataBuildType.READY) {
            initializeReady(builder);
        } else if (builder.type == MemberDataBuildType.PRELOADED) {
            initializePreload(builder);
        } else if (builder.type == MemberDataBuildType.PLAYERS_LEFT) {
            initializePlayersLeft(builder);
        }
    }

    /**
     * Initializes a full set of member data
     *
     * @param builder the builder
     */
    private void initializeMember(Builder builder) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();
        attributes.add("Location_s", builder.location);

        var heroData = buildHeroDefinition(builder);
        attributes.add("CampaignHero_j", Json.createObjectBuilder().add("CampaignHero", heroData).build());
        attributes.add("MatchmakingLevel_U", "0");
        attributes.add("ZoneInstanceId_s", "");
        attributes.add("HomeBaseVersion_U", "1");
        attributes.add("HasPreloadedAthena_b", builder.preloaded);

        var frontendData = Json.createObjectBuilder();
        frontendData.add("emoteItemDef", "None");
        frontendData.add("emoteItemDefEncryptionKey", "");
        frontendData.add("emoteSection", -1);
        attributes.add("FrontendEmote_j", Json.createObjectBuilder().add("FrontendEmote", frontendData.build()).build());
        attributes.add("NumAthenaPlayersLeft_U", "0");
        attributes.add("UtcTimeStartedMatchAthena_s", "0001-01-01T00:00:00.000Z");
        attributes.add("IsReadyAthena_b", builder.isReady);
        attributes.add("HiddenMatchmakingDelayMax_U", "0");
        attributes.add("ReadyInputType_s", builder.inputType);
        attributes.add("CurrentInputType_s", builder.inputType);

        var cosmeticLoadout = buildCharacterDefinition(builder, true);
        attributes.add("AthenaCosmeticLoadout_j", Json.createObjectBuilder().add("AthenaCosmeticLoadout", cosmeticLoadout).build());
        var bannerData = Json.createObjectBuilder();
        bannerData.add("bannerIconId", builder.bannerIcon);
        bannerData.add("bannerColorId", builder.bannerColor);
        bannerData.add("seasonLevel", builder.seasonLevel);
        attributes.add("AthenaBannerInfo_j", Json.createObjectBuilder().add("AthenaBannerInfo", bannerData.build()).build());

        var battlePassData = Json.createObjectBuilder();
        battlePassData.add("bHasPurchasedPass", builder.battlePass.hasPurchased());
        battlePassData.add("passLevel", builder.battlePass.getPassLevel());
        battlePassData.add("selfBoostXp", builder.battlePass.getSelfBoostXp());
        battlePassData.add("friendBoostXp", builder.battlePass.getFriendBoostXp());
        attributes.add("BattlePassInfo_j", Json.createObjectBuilder().add("BattlePassInfo", battlePassData.build()).build());
        attributes.add("Platform_j", Json.createObjectBuilder().add("Platform", Json.createObjectBuilder().add("platformStr", builder.platform).build()).build());
        attributes.add("PlatformUniqueId_s", "INVALID");
        attributes.add("PlatformSessionId_s", "");
        attributes.add("CrossplayPreference_s", "OptedIn");
        payload.add("Rev", PacketBuilder.getRevForType(PartyPacketType.PARTY_MEMBER_DATA));
        payload.add("Attrs", attributes.build());
        this.payload = payload.build();
    }

    /**
     * Copies the payload from another member
     *
     * @param builder the builder
     */
    private void initializeOtherMember(Builder builder) {
        this.payload = builder.payload;
    }

    /**
     * Initialize a skin change
     *
     * @param builder the builder
     */
    private void initializeSkinChange(Builder builder) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();

        var heroData = buildHeroDefinition(builder);
        attributes.add("CampaignHero_j", Json.createObjectBuilder().add("CampaignHero", heroData).build());
        var cosmeticLoadout = buildCharacterDefinition(builder, true);
        attributes.add("AthenaCosmeticLoadout_j", Json.createObjectBuilder().add("AthenaCosmeticLoadout", cosmeticLoadout).build());
        payload.add("Rev", PacketBuilder.getRevForType(PartyPacketType.PARTY_MEMBER_DATA));
        payload.add("Attrs", attributes.build());
        this.payload = payload.build();
    }

    /**
     * Initialize a backbling change
     *
     * @param builder the builder
     */
    private void initializeBackblingChange(Builder builder) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();
        attributes.add("CampaignHero_j", Json.createObjectBuilder().add("CampaignHero", Json.createObjectBuilder().build()).build());
        var cosmeticLoadout = buildCharacterDefinition(builder, false);
        attributes.add("AthenaCosmeticLoadout_j", Json.createObjectBuilder().add("AthenaCosmeticLoadout", cosmeticLoadout).build());
        payload.add("Rev", PacketBuilder.getRevForType(PartyPacketType.PARTY_MEMBER_DATA));
        payload.add("Attrs", attributes.build());
        this.payload = payload.build();
    }

    /**
     * Builds the character definition for backbling/skin
     *
     * @param builder          the builder
     * @param includeCharacter if the skin should be included
     * @return a {@link JsonObject} with skin/backbling values
     */
    private JsonObject buildCharacterDefinition(Builder builder, boolean includeCharacter) {
        var cosmeticLoadout = Json.createObjectBuilder();
        if (includeCharacter) {
            cosmeticLoadout.add("characterDefinition", "AthenaCharacterItemDefinition'/Game/Athena/Items/Cosmetics/Characters/" + builder.skin + "." + builder.skin + "'");
            cosmeticLoadout.add("characterDefinitionEncryptionKey", "");
        }
        cosmeticLoadout.add("backpackDefinition", "AthenaBackpackItemDefinition'/Game/Athena/Items/Cosmetics/Backpacks/" + builder.backbling + "." + builder.backbling + "'");
        cosmeticLoadout.add("backpackDefinitionEncryptionKey", "");
        cosmeticLoadout.add("cosmeticVariants", Json.createArrayBuilder().build());
        return cosmeticLoadout.build();
    }

    /**
     * Builds a hero definition based on skin
     *
     * @param builder the builder
     * @return a {@link JsonObject} with hero values
     */
    private JsonObject buildHeroDefinition(Builder builder) {
        var heroData = Json.createObjectBuilder();
        String heroName = builder.skin.replace("CID", "HID");
        heroData.add("heroItemInstanceId", "");
        heroData.add("heroType", "FortHeroType'/Game/Athena/Heroes/" + heroName + "." + heroName + "'");
        return heroData.build();
    }

    /**
     * Initializes a ready status.
     *
     * @param builder the builder
     */
    private void initializeReady(Builder builder) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();

        payload.add("Rev", PacketBuilder.getRevForType(PartyPacketType.PARTY_MEMBER_DATA));
        attributes.add("IsReadyAthena_b", builder.isReady);
        attributes.add("ReadyInputType_s", builder.inputType);
        payload.add("Attrs", attributes.build());
        this.payload = payload.build();
    }

    /**
     * Initializes a "preloaded" status.
     *
     * @param builder the builder
     */
    private void initializePreload(Builder builder) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();

        payload.add("Rev", PacketBuilder.getRevForType(PartyPacketType.PARTY_MEMBER_DATA));
        attributes.add("hasPreloadedAthena_b", builder.preloaded);
        payload.add("Attrs", attributes.build());
        this.payload = payload.build();
    }

    /**
     * Initializes a players left status
     *
     * @param builder the builder
     */
    private void initializePlayersLeft(Builder builder) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();

        payload.add("Rev", PacketBuilder.getRevForType(PartyPacketType.PARTY_MEMBER_DATA));
        attributes.add("NumAthenaPlayersLeft_U", builder.playersLeft);
        payload.add("Attrs", attributes.build());
        this.payload = payload.build();
    }

    public JsonObject getPayload() {
        return payload;
    }

    /**
     * Convenience method to change your skin.
     *
     * @param skin the skin
     * @return the member data to be sent
     */
    public static PartyMemberData changeSkin(Skin skin) {
        return new Builder(MemberDataBuildType.SKIN).setSkin(skin).build();
    }

    /**
     * Convenience method to change your backbling
     *
     * @param backbling the backbling
     * @return the member data to be sent
     */
    public static PartyMemberData changeBackbling(Backbling backbling) {
        return new Builder(MemberDataBuildType.BACKBLING).setBackbling(backbling).build();
    }

    /**
     * Convenience method to ready up
     *
     * @param isReady ready or not
     * @return the member data to be sent
     */
    public static PartyMemberData setReadyStatus(boolean isReady, InputType readyInputType) {
        return new Builder(MemberDataBuildType.READY).setIsReady(isReady).setInputType(readyInputType).build();
    }

    /**
     * Convenience method to ready up
     *
     * @param hasPreloaded preloaded or not
     * @return the member data to be sent
     */
    public static PartyMemberData setPreloaded(boolean hasPreloaded) {
        return new Builder(MemberDataBuildType.PRELOADED).setPreloaded(hasPreloaded).build();
    }

    /**
     * Create a new builder instance
     *
     * @param type the type
     * @return a {@link Builder} instance
     */
    public static Builder newBuilder(MemberDataBuildType type) {
        return new Builder(type);
    }

    public static final class Builder {

        private final MemberDataBuildType type;
        private String skin, backbling, bannerIcon, bannerColor, location;
        private String inputType, platform;
        private boolean isReady, preloaded;
        private int seasonLevel = 1, playersLeft;

        private BattlePass battlePass;
        private JsonObject payload;

        private Builder(MemberDataBuildType type) {
            this.type = type;
        }

        public Builder setSkin(String skinName) {
            this.skin = skinName;
            return this;
        }

        public Builder setSkin(Skin skin) {
            this.skin = skin.name();
            return this;
        }

        public Builder setBackbling(String backblingName) {
            this.backbling = backblingName;
            return this;
        }

        public Builder setBackbling(Backbling backbling) {
            this.backbling = backbling.getName();
            return this;
        }

        public Builder setInputType(InputType type) {
            this.inputType = type.getName();
            return this;
        }

        public Builder setPlatform(PlatformType platform) {
            this.platform = platform.name();
            return this;
        }

        public Builder setIsReady(boolean isReady) {
            this.isReady = isReady;
            return this;
        }

        public Builder setPreloaded(boolean hasPreloaded) {
            this.preloaded = hasPreloaded;
            return this;
        }

        public Builder setBannerIcon(String bannerIcon) {
            this.bannerIcon = bannerIcon;
            return this;
        }

        public Builder setBannerColor(String bannerColor) {
            this.bannerColor = bannerColor;
            return this;
        }

        public Builder setSeasonLevel(int seasonLevel) {
            this.seasonLevel = seasonLevel;
            return this;
        }

        public Builder setPlayersLeft(int playersLeft) {
            this.playersLeft = playersLeft;
            return this;
        }

        public Builder setBattlePass(BattlePass pass) {
            this.battlePass = pass;
            return this;
        }

        public Builder setPayload(JsonObject payload) {
            this.payload = payload;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public PartyMemberData build() {
            if (type == MemberDataBuildType.MEMBER) {
                if (battlePass == null) battlePass = new BattlePass();
                if (skin == null) skin = Skin.CID_001_Athena_Commando_F_Default.name();
                if (backbling == null) backbling = "None";
                if (inputType == null) inputType = InputType.KEYBOARD_AND_MOUSE.getName();
                if (platform == null) platform = PlatformType.WIN.name();
                // TODO: Find all banner ids/colors?
                if (bannerIcon == null) bannerIcon = "standardbanner15";
                if (bannerColor == null) bannerColor = "defaultcolor17";
                if (location == null) location = "PreLobby";
            }
            return new PartyMemberData(this);
        }

    }

}
