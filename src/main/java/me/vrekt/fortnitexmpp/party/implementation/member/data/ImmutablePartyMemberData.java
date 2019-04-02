package me.vrekt.fortnitexmpp.party.implementation.member.data;

import me.vrekt.fortnitexmpp.utility.JsonUtility;

import javax.json.JsonObject;

/**
 * Used for getting information about another party member.
 * This is Immutable meaning nothing here can be changed.
 */
public final class ImmutablePartyMemberData {

    private String skin, backbling, location, currentEmote, inputType, bannerIconId, bannerColorId, platform;
    private boolean hasPreloaded, isReady, hasPurchasedPass;
    private int playersLeft, seasonLevel, passLevel, selfBoostXp, friendBoostXp;
    private final JsonObject payload;

    /**
     * Adapt the provided {@code payload} and set the fields provided.
     *
     * @param payload the payload
     * @return a new {@link ImmutablePartyMemberData}
     */
    public static ImmutablePartyMemberData adaptFrom(final JsonObject payload) {
        final var innerPayload = JsonUtility.getObject("payload", payload);
        final var attributes = JsonUtility.getObject("Attrs", innerPayload.orElse(null));
        if (innerPayload.isEmpty() || attributes.isEmpty()) return null;
        return new ImmutablePartyMemberData(payload, attributes.get());
    }

    /**
     * Initialize this data set, read everything that is useful and available.
     *
     * @param attributes the attributes
     */
    private ImmutablePartyMemberData(final JsonObject payload, final JsonObject attributes) {
        this.payload = payload;
        JsonUtility.getString("Location_s", attributes).ifPresent(location -> this.location = location);
        JsonUtility.getBoolean("HasPreloadedAthena_b", attributes).ifPresent(hasPreloaded -> this.hasPreloaded = hasPreloaded);
        JsonUtility.getObject("FrontendEmote_j", "FrontendEmote", attributes)
                .ifPresent(object -> JsonUtility.getString("emoteItemDef", attributes).ifPresent(currentEmote -> this.currentEmote = currentEmote));
        JsonUtility.getString("NumAthenaPlayersLeft_U", attributes).ifPresent(playersLeft -> this.playersLeft = Integer.valueOf(playersLeft));
        JsonUtility.getBoolean("IsReadyAthena_b", attributes).ifPresent(isReady -> this.isReady = isReady);
        JsonUtility.getString("CurrentInputType_s", attributes).ifPresent(inputType -> this.inputType = inputType);

        // skin, backbling, pickaxe.
        // TODO: Add pickaxe definition? kinda useless.
        JsonUtility.getObject("AthenaCosmeticLoadout_j", "AthenaCosmeticLoadout", attributes)
                .ifPresent(object -> {
                    JsonUtility.getString("characterDefinition", object).ifPresent(skin -> this.skin = skin);
                    JsonUtility.getString("backpackDefinition", object).ifPresent(backbling -> this.backbling = backbling);
                });
        // banner stuff
        JsonUtility.getObject("AthenaBannerInfo_j", "AthenaBannerInfo", attributes)
                .ifPresent(object -> {
                    JsonUtility.getString("bannerIconId", object).ifPresent(bannerIconId -> this.bannerIconId = bannerIconId);
                    JsonUtility.getString("bannerColorId", object).ifPresent(bannerColorId -> this.bannerColorId = bannerColorId);
                    JsonUtility.getInt("seasonLevel", object).ifPresent(seasonLevel -> this.seasonLevel = seasonLevel);
                });
        // battle pass information
        JsonUtility.getObject("BattlePassInfo_j", "BattlePassInfo", attributes)
                .ifPresent(object -> {
                    JsonUtility.getBoolean("bHasPurchasedPass", object).ifPresent(hasPurchasedPass -> this.hasPurchasedPass = hasPurchasedPass);
                    JsonUtility.getInt("passLevel", object).ifPresent(passLevel -> this.passLevel = passLevel);
                    JsonUtility.getInt("selfBoostXp", object).ifPresent(selfBoostXp -> this.selfBoostXp = selfBoostXp);
                    JsonUtility.getInt("friendBoostXp", object).ifPresent(friendBoostXp -> this.friendBoostXp = friendBoostXp);
                });
        // their platform.
        JsonUtility.getObject("Platform_j", "Platform", attributes)
                .ifPresent(object -> JsonUtility.getString("platformStr", object).ifPresent(platform -> this.platform = platform));
    }

    public JsonObject payload() {
        return payload;
    }

    public String getSkin() {
        return skin;
    }

    public String getBackbling() {
        return backbling;
    }

    public String getLocation() {
        return location;
    }

    public String getCurrentEmote() {
        return currentEmote;
    }

    public String getInputType() {
        return inputType;
    }

    public String getBannerIconId() {
        return bannerIconId;
    }

    public String getBannerColorId() {
        return bannerColorId;
    }

    public String getPlatform() {
        return platform;
    }

    public boolean hasPreloaded() {
        return hasPreloaded;
    }

    public boolean isReady() {
        return isReady;
    }

    public boolean hasPurchasedPass() {
        return hasPurchasedPass;
    }

    public int getPlayersLeft() {
        return playersLeft;
    }

    public int getSeasonLevel() {
        return seasonLevel;
    }

    public int getPassLevel() {
        return passLevel;
    }

    public int getSelfBoostXp() {
        return selfBoostXp;
    }

    public int getFriendBoostXp() {
        return friendBoostXp;
    }
}
