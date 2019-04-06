package me.vrekt.fortnitexmpp.utility;

import com.google.common.flogger.FluentLogger;

public final class Logging {

    public static void logInfoIfApplicable(FluentLogger.Api logger, boolean log, String message) {
        if (!log) return;
        logger.log(message);
    }

}
