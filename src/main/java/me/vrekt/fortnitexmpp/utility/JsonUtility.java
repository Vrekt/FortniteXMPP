package me.vrekt.fortnitexmpp.utility;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.Optional;

/**
 * Utility class for getting values from JSON.
 */
public final class JsonUtility {

    public static Optional<String> getString(String name, JsonObject object) {
        if (name == null || object == null) return Optional.empty();
        return object.containsKey(name) ? Optional.ofNullable(object.getString(name)) : Optional.empty();
    }

    public static Optional<Integer> getInt(String name, JsonObject object) {
        if (name == null || object == null) return Optional.empty();
        return object.containsKey(name) ? Optional.of(object.getJsonNumber(name).intValue()) : Optional.empty();
    }

    public static Optional<Long> getLong(String name, JsonObject object) {
        if (name == null || object == null) return Optional.empty();
        return object.containsKey(name) ? Optional.of(object.getJsonNumber(name).longValue()) : Optional.empty();
    }

    public static Optional<JsonObject> getObject(String name, JsonObject object) {
        if (name == null || object == null) return Optional.empty();
        return object.containsKey(name) ? Optional.ofNullable(object.getJsonObject(name)) : Optional.empty();
    }

    public static Optional<JsonArray> getArray(String name, JsonObject object) {
        if (name == null || object == null) return Optional.empty();
        return object.containsKey(name) ? Optional.ofNullable(object.getJsonArray(name)) : Optional.empty();
    }

    public static Optional<Boolean> getBoolean(String name, JsonObject object) {
        if (name == null || object == null) return Optional.empty();
        return object.containsKey(name) ? Optional.of(object.getBoolean(name)) : Optional.empty();
    }

}
