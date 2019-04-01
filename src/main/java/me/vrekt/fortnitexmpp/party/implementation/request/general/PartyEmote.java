package me.vrekt.fortnitexmpp.party.implementation.request.general;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.party.implementation.member.dance.Dance;
import me.vrekt.fortnitexmpp.party.implementation.member.emoji.Emoji;
import me.vrekt.fortnitexmpp.party.implementation.request.PartyRequest;
import me.vrekt.fortnitexmpp.party.implementation.request.RequestBuilder;
import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;

public final class PartyEmote implements PartyRequest {

    private final String payload;

    /**
     * Initializes this request
     *
     * @param party the party to emote in
     * @param dance the dance to use
     * @param stage the stage, {@code Stage.START} to start the dance, {@code Stage.STOP} to stop it.
     */
    public PartyEmote(final Party party, final Dance dance, final Stage stage) {
        this.payload = build(party, true, dance.getName(), stage.code);
    }

    /**
     * Initializes this request
     *
     * @param party     the party to emote in
     * @param danceName the name of the dance to use
     * @param stage     the stage, {@code Stage.START} to start the dance, {@code Stage.STOP} to stop it.
     */
    public PartyEmote(final Party party, final String danceName, final Stage stage) {
        this.payload = build(party, true, danceName, stage.code);
    }

    /**
     * Initialize this request
     *
     * @param party the party to emote in
     * @param emoji the emoji to throw
     */
    public PartyEmote(final Party party, final Emoji emoji) {
        this.payload = build(party, false, emoji.getName(), Stage.START.code);
    }

    /**
     * Initialize this request
     *
     * @param party     the party to emote in
     * @param emojiName the name of the emoji to throw
     */
    public PartyEmote(final Party party, final String emojiName) {
        this.payload = build(party, false, emojiName, Stage.START.code);
    }

    /**
     * Builds the payload for this request.
     *
     * @param party   the party
     * @param isDance {@code true} if this request is a dance.
     * @param name    the name of the emote or dance.
     * @param stage   the stage to use
     * @return a string representing the payload to send
     */
    private String build(final Party party, final boolean isDance, final String name, final int stage) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();

        var emoteData = Json.createObjectBuilder();
        emoteData.add("emoteItemDef", stage == -1 ? "None" :
                isDance ? "AthenaDanceItemDefinition'/Game/Athena/Items/Cosmetics/Dances/" + name + "." + name + "'" :
                        "AthenaEmojiItemDefinition'/Game/Athena/Items/Cosmetics/Dances/Emoji/" + name + "." + name + "'");
        emoteData.add("emoteItemDefEncryptionKey", "");
        emoteData.add("emoteSection", stage);
        attributes.add("FrontendEmote_j", Json.createObjectBuilder().add("FrontendEmote", emoteData.build()).build());

        payload.add("Rev", RequestBuilder.getRevisionFor(PartyType.PARTY_MEMBER_DATA));
        payload.add("Attrs", attributes.build());
        return RequestBuilder.buildRequestDoublePayload(party.partyId(), payload.build(), PartyType.PARTY_MEMBER_DATA).toString();
    }

    @Override
    public String payload() {
        return payload;
    }

    /**
     * Stage of the emote or dance.
     * Stopping an emoji isn't required but for a dance it is.
     */
    public enum Stage {
        START(-2), STOP(-1);

        private final int code;

        Stage(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

}
