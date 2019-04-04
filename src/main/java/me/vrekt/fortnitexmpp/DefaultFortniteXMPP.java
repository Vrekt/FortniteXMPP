package me.vrekt.fortnitexmpp;

import com.google.common.flogger.FluentLogger;
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite;
import me.vrekt.fortnitexmpp.chat.ChatResource;
import me.vrekt.fortnitexmpp.chat.DefaultChatResource;
import me.vrekt.fortnitexmpp.exception.FortniteAuthenticationException;
import me.vrekt.fortnitexmpp.exception.XMPPAuthenticationException;
import me.vrekt.fortnitexmpp.friend.DefaultFriendResource;
import me.vrekt.fortnitexmpp.friend.FriendResource;
import me.vrekt.fortnitexmpp.party.DefaultPartyResource;
import me.vrekt.fortnitexmpp.party.PartyResource;
import me.vrekt.fortnitexmpp.presence.DefaultPresenceResource;
import me.vrekt.fortnitexmpp.presence.PresenceResource;
import org.apache.commons.lang3.RandomStringUtils;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingManager;
import org.jxmpp.jid.EntityFullJid;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class DefaultFortniteXMPP implements FortniteXMPP {

    private final ConnectionErrorListener errorListener = new ConnectionErrorListener();
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private final AtomicBoolean reconnecting = new AtomicBoolean(false);
    private final AtomicBoolean loadRoster = new AtomicBoolean(true);

    private DefaultFortnite.Builder fortniteBuilder;

    private Fortnite fortnite;
    private final AppType appType;
    private final PlatformType platformType;

    private XMPPTCPConnection connection;
    private EntityFullJid user;
    private Account account;

    private DefaultChatResource chatResource;
    private DefaultFriendResource friendResource;
    private DefaultPartyResource partyResource;
    private DefaultPresenceResource presenceResource;

    private boolean useExperimentalStuff;

    /**
     * Creates a new instance of {@link FortniteXMPP}
     *
     * @param builder      the builder instance used to authenticate to fortnite.
     * @param appType      the application type to use. Either {@code AppType.FORTNITE} or {@code AppType.LAUNCHER}
     * @param platformType the type of platform
     */
    DefaultFortniteXMPP(final DefaultFortnite.Builder builder, final AppType appType, final PlatformType platformType) throws FortniteAuthenticationException {
        try {
            this.fortniteBuilder = builder;
            this.fortnite = builder.build();
            this.appType = appType;
            this.platformType = platformType;
        } catch (final IOException exception) {
            throw new FortniteAuthenticationException("Could not authenticate with Fortnite.", exception);
        }
    }

    /**
     * Creates a new instance of {@link FortniteXMPP}
     *
     * @param emailAddress the email address to use to login
     * @param password     the password to use to login
     * @param appType      the application type to use. Either {@code AppType.FORTNITE} or {@code AppType.LAUNCHER}
     * @param platformType the type of platform
     */
    DefaultFortniteXMPP(final String emailAddress, final String password, final AppType appType, final PlatformType platformType) throws FortniteAuthenticationException {
        try {
            this.fortniteBuilder = DefaultFortnite.Builder.newInstance(emailAddress, password);
            this.fortnite = fortniteBuilder.build();
            this.appType = appType;
            this.platformType = platformType;
        } catch (final IOException exception) {
            throw new FortniteAuthenticationException("Could not authenticate with Fortnite.", exception);
        }
    }

    @Override
    public void connect() throws XMPPAuthenticationException {
        try {
            reconnecting.set(true);
            fortnite.account().findOneBySessionAccountId().ifPresent(acc -> this.account = acc);
            final var accessToken = fortnite.session().accessToken();
            final var random = RandomStringUtils.randomAlphanumeric(32).toUpperCase();
            final var resource = "V2:" + appType.getName() + ":" + platformType.name() + (useExperimentalStuff ? "::" + random : "");

            connection = new XMPPTCPConnection(XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(account.accountId(), accessToken)
                    .setXmppDomain(SERVICE_DOMAIN)
                    .setHost(SERVICE_HOST)
                    .setPort(SERVICE_PORT)
                    .setResource(resource)
                    .setConnectTimeout(60000)
                    .build());

            connection.addConnectionListener(errorListener);

            final var roster = Roster.getInstanceFor(connection);
            final var load = loadRoster.get();

            if (!load) roster.setRosterLoadedAtLogin(false);
            if (!load) roster.setSubscriptionMode(Roster.SubscriptionMode.manual);

            connection.connect().login();
            connection.setReplyTimeout(120000);
            this.user = connection.getUser();

            if (load) if (!roster.isLoaded()) roster.reloadAndWait();

            // set the ping interval, makes a more stable connection.
            final var pingManager = PingManager.getInstanceFor(connection);
            pingManager.setPingInterval(60);

            chatResource = new DefaultChatResource(this);
            friendResource = new DefaultFriendResource(this);
            partyResource = new DefaultPartyResource(this);
            presenceResource = new DefaultPresenceResource(this);


            connection.sendStanza(new Presence(Presence.Type.available));
            LOGGER.atInfo().log("Connected to the XMPP service successfully.");
            reconnecting.set(false);
        } catch (final IOException | SmackException | XMPPException | InterruptedException exception) {
            throw new XMPPAuthenticationException("Could not connect to the XMPP service.", exception);
        }

    }

    @Override
    public void reestablishConnectionOnceAfter(final long timeout, final TimeUnit unit) {
        CompletableFuture.delayedExecutor(timeout, unit).execute(this::reconnectClean);
    }

    @Override
    public void connectAsync(final Consumer<Boolean> callback) {
        CompletableFuture.runAsync(() -> {
            try {
                connect();
                if (callback != null) callback.accept(true);
            } catch (final XMPPAuthenticationException exception) {
                if (callback != null) callback.accept(false);
            }
        });
    }

    @Override
    public void keepConnectionAlive(final long reconnectionPeriod, final TimeUnit unit) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::reconnectClean, reconnectionPeriod, reconnectionPeriod, unit);
    }

    @Override
    public boolean isReconnecting() {
        return reconnecting.get();
    }

    /**
     * Authenticates with Fortnite again and then attempts to reconnect.
     */
    private void reconnectClean() {
        LOGGER.atInfo().log("Reconnecting to the XMPP service!");
        disconnect();

        reconnecting.set(true);
        try {
            this.fortnite = fortniteBuilder.build();
            connect();
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        fortnite.close();
        chatResource.close();
        friendResource.close();
        partyResource.close();
        presenceResource.close();
        connection.disconnect();
    }

    @Override
    public void logExceptionsAndWarnings(final boolean log) {
        partyResource.logExceptionsAndWarnings(log);
        friendResource.logExceptionsAndWarnings(log);
    }

    @Override
    public void setLoadRoster(boolean loadRoster) {
        this.loadRoster.set(loadRoster);
    }

    @Override
    public Fortnite fortnite() {
        return fortnite;
    }

    @Override
    public String accountId() {
        return account.accountId();
    }

    @Override
    public String displayName() {
        return account.displayName();
    }

    @Override
    public XMPPTCPConnection connection() {
        return connection;
    }

    @Override
    public EntityFullJid user() {
        return user;
    }

    @Override
    public ChatResource chat() {
        return chatResource;
    }

    @Override
    public FriendResource friend() {
        return friendResource;
    }

    @Override
    public PartyResource party() {
        return partyResource;
    }

    @Override
    public PresenceResource presence() {
        return presenceResource;
    }

    @Override
    public void useExperimentalStuff() {
        this.useExperimentalStuff = true;
    }

    private final class ConnectionErrorListener implements ConnectionListener {
        @Override
        public void connected(XMPPConnection connection) {
            //
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            //
        }

        @Override
        public void connectionClosed() {
            //
        }

        @Override
        public void connectionClosedOnError(Exception exception) {
            LOGGER.atSevere().log("Connection closed with error! ", exception.getMessage());
            LOGGER.atInfo().log("Attempting reconnect in 5 seconds!");
            reconnecting.set(true);

            CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(DefaultFortniteXMPP.this::reconnectClean);
        }
    }
}
