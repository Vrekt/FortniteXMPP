package me.vrekt.fortnitexmpp.implementation.party.packet.general;

import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.member.dance.Dance;
import me.vrekt.fortnitexmpp.implementation.party.member.emoji.Emoji;
import me.vrekt.fortnitexmpp.implementation.party.packet.PacketBuilder;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacket;
import me.vrekt.fortnitexmpp.implementation.party.packet.PartyPacketType;
import org.jxmpp.jid.Jid;

import javax.json.Json;

/**
 * A packet used for emoting/dancing.
 */
public final class PacketPartyEmote implements PartyPacket {

    private String payload;
    private Jid to;

    /**
     * Initialize the packet
     *
     * @param party the party dancing in.
     * @param dance the dance
     * @param stage the stage, {@code Stage.START} to start the dance, {@code Stage.STOP} to stop the dance.
     */
    public PacketPartyEmote(Party party, Dance dance, Stage stage) {
        final var type = PartyPacketType.PARTY_MEMBER_DATA;
        this.to = party.getFrom();
        initializePayload(party, true, dance.getName(), stage.code, type);
    }

    /**
     * Initialize the packet
     *
     * @param party the party emoting in
     * @param emoji the emoji
     */
    public PacketPartyEmote(Party party, Emoji emoji) {
        final var type = PartyPacketType.PARTY_MEMBER_DATA;
        this.to = party.getFrom();
        initializePayload(party, false, emoji.getName(), Stage.START.code, type);
    }

    /**
     * Initialize the payload based on if it is a dance or emote
     *
     * @param party   the party
     * @param isDance if the emote is a dance or not
     * @param name    the name of the emote/dance
     * @param stage   the stage, -1 = none/stop -2 = emote
     * @param type    the type of packet
     */
    private void initializePayload(Party party, boolean isDance, String name, int stage, PartyPacketType type) {
        var payload = Json.createObjectBuilder();
        var attributes = Json.createObjectBuilder();

        var emoteData = Json.createObjectBuilder();
        emoteData.add("emoteItemDef", stage == -1 ? "None" :
                isDance ? "AthenaDanceItemDefinition'/Game/Athena/Items/Cosmetics/Dances/" + name + "." + name + "'" :
                        "AthenaEmojiItemDefinition'/Game/Athena/Items/Cosmetics/Dances/Emoji/" + name + "." + name + "'");
        emoteData.add("emoteItemDefEncryptionKey", "");
        emoteData.add("emoteSection", stage);
        attributes.add("FrontendEmote_j", Json.createObjectBuilder().add("FrontendEmote", emoteData.build()).build());

        payload.add("Rev", 1);
        payload.add("Attrs", attributes.build());
        this.payload = PacketBuilder.buildPacketDoublePayload(party.getId(), payload.build(), type).toString();
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public Jid getTo() {
        return to;
    }

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
