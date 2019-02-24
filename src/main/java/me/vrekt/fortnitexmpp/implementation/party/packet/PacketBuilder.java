package me.vrekt.fortnitexmpp.implementation.party.packet;

import javax.json.Json;
import javax.json.JsonObject;
import java.time.LocalDateTime;

public final class PacketBuilder {

    /**
     * Builds a packet with the payload and type
     *
     * @param payload the payload to send
     * @param type    the type of packet
     * @return a {@link JsonObject} payload to send.
     */
    public static JsonObject buildPacket(JsonObject payload, PartyPacketType type) {
        var object = Json.createObjectBuilder();
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
        var object = Json.createObjectBuilder();
        object.add("type", type.getName());
        object.add("payload", Json.createObjectBuilder().add("partyId", partyId).add("payload", payload).build());
        object.add("timestamp", LocalDateTime.now().toString());
        return object.build();
    }

}
