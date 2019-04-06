package me.vrekt.fortnitexmpp.provider;

import java.util.concurrent.TimeUnit;

public final class FortniteXMPPConfiguration {

    private final boolean enableLogging, loadRoster, reconnectOnError;
    private long keepAlivePeriod = -1, reconnectionWaitTime = 5;
    private TimeUnit timeUnit;

    /**
     * @return the default configuration to use. {@code logExceptions} = {@code true} / {@code loadRoster} = {@code true}
     */
    public static FortniteXMPPConfiguration defaultConfiguration() {
        return new FortniteXMPPConfiguration(true, true, true);
    }

    /**
     * Initialize this configuration
     *
     * @param enableLogging {@code true} if debug logging should be enabled
     * @param loadRoster    {@code true} if the roster should be loaded on login.
     */
    public FortniteXMPPConfiguration(final boolean enableLogging, final boolean loadRoster, final boolean reconnectOnError) {
        this.enableLogging = enableLogging;
        this.loadRoster = loadRoster;
        this.reconnectOnError = reconnectOnError;
    }

    /**
     * Initialize this configuration
     *
     * @param enableLogging        {@code true} if debug logging should be enabled
     * @param loadRoster           {@code true} if the roster should be loaded on login.
     * @param reconnectOnError     {@code true} if a reconnect should be attempted on a connection error
     * @param reconnectionWaitTime if an error would to occur that would close the connection then this parameter specifies how long to wait before reconnecting in seconds.
     */
    public FortniteXMPPConfiguration(final boolean enableLogging, final boolean loadRoster, final boolean reconnectOnError, final long reconnectionWaitTime) {
        this(enableLogging, loadRoster, reconnectOnError);
        this.reconnectionWaitTime = reconnectionWaitTime;
    }

    /**
     * Initialize this configuration
     *
     * @param enableLogging    {@code true} if debug logging should be enabled
     * @param loadRoster       {@code true} if the roster should be loaded on login.
     * @param reconnectOnError {@code true} if a reconnect should be attempted on a connection error
     * @param keepAlivePeriod  the time between reconnects.
     *                         e.g: if the time unit is {@code TimeUnit.HOURS} and {@code keepAlivePeriod} is 7
     *                         then every 7 hours the service will disconnect from XMPP, renew the fortnite access token and reconnect.
     * @param timeUnit         the time unit to use
     */
    public FortniteXMPPConfiguration(final boolean enableLogging, final boolean loadRoster, final boolean reconnectOnError, final long keepAlivePeriod, final TimeUnit timeUnit) {
        this(enableLogging, loadRoster, reconnectOnError);
        this.keepAlivePeriod = keepAlivePeriod;
        this.timeUnit = timeUnit;
    }

    /**
     * Initialize this configuration
     *
     * @param enableLogging        {@code true} if debug logging should be enabled
     * @param loadRoster           {@code true} if the roster should be loaded on login.
     * @param reconnectOnError     {@code true} if a reconnect should be attempted on a connection error
     * @param keepAlivePeriod      the time between reconnects.
     *                             e.g: if the time unit is {@code TimeUnit.HOURS} and {@code keepAlivePeriod} is 7
     *                             then every 7 hours the service will disconnect from XMPP, renew the fortnite access token and reconnect.
     * @param timeUnit             the time unit to use
     * @param reconnectionWaitTime if an error would to occur that would close the connection then this parameter specifies how long to wait before reconnecting in seconds.
     */
    public FortniteXMPPConfiguration(final boolean enableLogging, final boolean loadRoster, final boolean reconnectOnError, final long keepAlivePeriod, final TimeUnit timeUnit,
                                     final long reconnectionWaitTime) {
        this(enableLogging, loadRoster, reconnectOnError, keepAlivePeriod, timeUnit);
        this.reconnectionWaitTime = reconnectionWaitTime;
    }

    public boolean doEnableLogging() {
        return enableLogging;
    }

    public boolean doLoadRoster() {
        return loadRoster;
    }

    public boolean doReconnectOnError() {
        return reconnectOnError;
    }

    public boolean doKeepAlive() {
        return keepAlivePeriod != -1 && timeUnit != null;
    }

    public long getKeepAlivePeriod() {
        return keepAlivePeriod;
    }

    public long getReconnectionWaitTime() {
        return reconnectionWaitTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
