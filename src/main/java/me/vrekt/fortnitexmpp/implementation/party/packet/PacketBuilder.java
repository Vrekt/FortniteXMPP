package me.vrekt.fortnitexmpp.implementation.party.packet;

import javax.json.Json;
import javax.json.JsonObject;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public final class PacketBuilder {

    private static final Map<PartyPacketType, Integer> REVS = new HashMap<>();

    /**
     * Builds a packet with the payload and type
     *
     * @param payload the payload to send
     * @param type    the type of packet
     * @return a {@link JsonObject} payload to send.
     */
    public static JsonObject buildPacket(JsonObject payload, PartyPacketType type) {
        REVS.put(type, REVS.getOrDefault(type, 0) + 1);
        final var object = Json.createObjectBuilder();
        object.add("type", type.getName());
        object.add("payload", payload);
        object.add("timestamp", LocalDateTime.now().toString());
        return object.build();
    }

    /**
     * Builds a packet with two payloads.
     * Example: type: ... payload: "partyId", payload: "important data"
     *
     * @param partyId the party ID
     * @param payload the payload to send
     * @param type    the type of packet
     * @return a {@link JsonObject} payload to send.
     */
    public static JsonObject buildPacketDoublePayload(String partyId, JsonObject payload, PartyPacketType type) {
        REVS.put(type, REVS.getOrDefault(type, 0) + 1);
        final var object = Json.createObjectBuilder();
        object.add("type", type.getName());
        object.add("payload", Json.createObjectBuilder().add("partyId", partyId).add("payload", payload).build());
        object.add("timestamp", LocalDateTime.now().toString());
        return object.build();
    }

    /**
     * Return the revision for whatever packet.
     *
     * @param type the typeR
     * @return the revision for whatever packet.
     */
    public static int getRevForType(PartyPacketType type) {
        return REVS.getOrDefault(type, 1);
    }

}
