package me.vrekt.fortnitexmpp.utility;

import me.vrekt.fortnitexmpp.type.PlatformType;
import org.apache.commons.lang3.StringUtils;

public final class FindPlatformUtility {

    /**
     * Attempts to get the users platform from the resource.
     *
     * @param resource the resource
     * @return {@code null} if no platform was found.
     */
    public static PlatformType getPlatformForResource(final String resource) {
        final var split = StringUtils.split(resource, ":");
        if (split == null || split.length < 3) {
            return null;
        } else {
            try {
                return PlatformType.valueOf(split[2]);
            } catch (final IllegalArgumentException exception) {
                return null;
            }
        }
    }
}
