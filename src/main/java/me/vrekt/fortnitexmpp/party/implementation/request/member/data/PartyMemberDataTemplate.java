package me.vrekt.fortnitexmpp.party.implementation.request.member.data;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.member.battlepass.BattlePass;
import me.vrekt.fortnitexmpp.party.implementation.member.cosmetic.Backbling;
import me.vrekt.fortnitexmpp.party.implementation.member.cosmetic.Skin;
import me.vrekt.fortnitexmpp.party.implementation.member.input.InputType;
import me.vrekt.fortnitexmpp.party.implementation.request.member.PartyMemberData;
import me.vrekt.fortnitexmpp.type.PlatformType;

/**
 * A utility class used for holding values.
 * For example hold a certain skin and backbling,
 * then later you can change the party ID of who its going to and build.
 */
public final class PartyMemberDataTemplate {

    private final String skin, backbling;
    private final InputType inputType;
    private final BattlePass battlePass;
    private final PlatformType platformType;

    /**
     * Create this template.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"}
     * @param inputType    the desired input type
     * @param battlePass   the battle pass to use, or {@code null}
     * @param platformType the desired platform
     */
    public PartyMemberDataTemplate(final String skin, final String backbling, final InputType inputType, final BattlePass battlePass, final PlatformType platformType) {
        this.skin = skin;
        this.backbling = backbling;
        this.inputType = inputType;
        this.battlePass = battlePass;
        this.platformType = platformType;
    }

    /**
     * Create this template.
     *
     * @param skin         the skin to use
     * @param backbling    the backbling to use, or {@code "None"}
     * @param inputType    the desired input type
     * @param battlePass   the battle pass to use, or {@code null}
     * @param platformType the desired platform
     */
    public PartyMemberDataTemplate(final Skin skin, final Backbling backbling, final InputType inputType, final BattlePass battlePass, final PlatformType platformType) {
        this(skin.name(), backbling.name(), inputType, battlePass, platformType);
    }

    /**
     * Build this template.
     *
     * @param party the party of who this template is going to.
     * @return a new {@link PartyMemberData} instance
     */
    public PartyMemberData build(final Party party) {
        return PartyMemberData.create(party.partyId(), skin, backbling, battlePass, inputType, platformType);
    }

    /**
     * Build this template.
     *
     * @param partyId the party of who this template is going to.
     * @return a new {@link PartyMemberData} instance
     */
    public PartyMemberData build(final String partyId) {
        return PartyMemberData.create(partyId, skin, backbling, battlePass, inputType, platformType);
    }

}
