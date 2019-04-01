package me.vrekt.fortnitexmpp.party.implementation.request;

import me.vrekt.fortnitexmpp.party.type.PartyType;

import javax.json.Json;
import javax.json.JsonObject;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public final class RequestBuilder {

    private static final Map<PartyType, Integer> REVISIONS = new HashMap<>();

    /**
     * Builds a request with the payload and type
     *
     * @param payload the payload to send
     * @param type    the type of packet
     * @return a {@link JsonObject} payload to send.
     */
    public static JsonObject buildRequest(final JsonObject payload, final PartyType type) {
        REVISIONS.put(type, REVISIONS.getOrDefault(type, 0) + 1);
        final var object = Json.createObjectBuilder();
        object.add("type", type.getName());
        object.add("payload", payload);
        object.add("timestamp", LocalDateTime.now().toString());
        return object.build();
    }

    /**
     * Builds a request with two payloads.
     * Example: type: ... payload: "partyId", payload: "important data"
     *
     * @param partyId the party ID
     * @param payload the payload to send
     * @param type    the type of packet
     * @return a {@link JsonObject} payload to send.
     */
    public static JsonObject buildRequestDoublePayload(final String partyId, final JsonObject payload, final PartyType type) {
        REVISIONS.put(type, REVISIONS.getOrDefault(type, 0) + 1);
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
    public static int getRevisionFor(PartyType type) {
        return REVISIONS.getOrDefault(type, 1);
    }

}
