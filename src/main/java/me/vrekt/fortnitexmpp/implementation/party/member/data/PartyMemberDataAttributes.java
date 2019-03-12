package me.vrekt.fortnitexmpp.implementation.party.member.data;

import me.vrekt.fortnitexmpp.implementation.party.member.battlepass.BattlePass;
import me.vrekt.fortnitexmpp.utility.JsonUtility;

import javax.json.JsonObject;
import java.util.Optional;

public final class PartyMemberDataAttributes {

    private String skinName, backbling, pickaxe, emoteDefinition, bannerIconId, bannerColorId, platform;
    private int seasonLevel, matchmakingDelay, playersLeft;
    private final BattlePass battlePass = new BattlePass();

    public PartyMemberDataAttributes(JsonObject p) {
        final var payload = p.getJsonObject("payload").getJsonObject("Attrs");

        JsonUtility.getObject("AthenaCosmeticLoadout_j", "AthenaCosmeticLoadout", payload).ifPresent(object -> {
            JsonUtility.getString("characterDefinition", object).ifPresent(skinName -> this.skinName = skinName);
            JsonUtility.getString("backpackDefinition", object).ifPresent(backbling -> this.backbling = backbling);
            JsonUtility.getString("pickaxeDefinition", object).ifPresent(pickaxe -> this.pickaxe = pickaxe);
        });

        JsonUtility.getObject("FrontendEmote_j", "FrontendEmote", payload).ifPresent(object -> JsonUtility.getString("emoteItemDef", object).ifPresent(emoteDefinition -> this.emoteDefinition = emoteDefinition));
        JsonUtility.getObject("AthenaBannerInfo_j", "AthenaBannerInfo", payload).ifPresent(object -> {
            JsonUtility.getString("bannerIconId", object).ifPresent(bannerIconId -> this.bannerIconId = bannerIconId);
            JsonUtility.getString("bannerColorId", object).ifPresent(bannerColorId -> this.bannerColorId = bannerColorId);
            JsonUtility.getInt("seasonLevel", object).ifPresent(seasonLevel -> this.seasonLevel = seasonLevel);
        });

        JsonUtility.getObject("Platform_j", "Platform", payload).ifPresent(object -> JsonUtility.getString("platformStr", object).ifPresent(platform -> this.platform = platform));

        JsonUtility.getString("HiddenMatchmakingDelayMax_U", payload).ifPresent(string -> this.matchmakingDelay = Integer.valueOf(string));
        JsonUtility.getString("NumAthenaPlayersLeft_U", payload).ifPresent(string -> this.playersLeft = Integer.valueOf(string));

        JsonUtility.getObject("BattlePassInfo_j", "BattlePassInfo", payload).ifPresent(object -> {
            JsonUtility.getBoolean("bHasPurchasedPass", object).ifPresent(battlePass::setHasPurchased);
            JsonUtility.getInt("passLevel", object).ifPresent(battlePass::setPassLevel);
            JsonUtility.getInt("selfBoostXp", object).ifPresent(battlePass::setSelfBoostXp);
            JsonUtility.getInt("friendBoostXp", object).ifPresent(battlePass::setFriendBoostXp);
        });

    }

    public Optional<String> getSkinName() {
        return Optional.ofNullable(skinName);
    }

    public Optional<String> getBackbling() {
        return Optional.ofNullable(backbling);
    }

    public Optional<String> getPickaxe() {
        return Optional.ofNullable(pickaxe);
    }

    public Optional<String> getEmoteDefinition() {
        return Optional.ofNullable(emoteDefinition);
    }

    public Optional<String> getBannerIconId() {
        return Optional.ofNullable(bannerIconId);
    }

    public Optional<String> getBannerColorId() {
        return Optional.ofNullable(bannerColorId);
    }

    public Optional<String> getPlatform() {
        return Optional.ofNullable(platform);
    }

    public int getSeasonLevel() {
        return seasonLevel;
    }

    public int getMatchmakingDelay() {
        return matchmakingDelay;
    }

    public int getPlayersLeft() {
        return playersLeft;
    }

    public BattlePass getBattlePass() {
        return battlePass;
    }
}
