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
import me.vrekt.fortnitexmpp.provider.FortniteXMPPConfiguration;
import me.vrekt.fortnitexmpp.utility.Logging;
import org.apache.commons.lang3.RandomStringUtils;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jxmpp.jid.EntityFullJid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;

public final class DefaultFortniteXMPP implements FortniteXMPP {

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    // handles reconnection stuff.
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
    private ScheduledFuture<?> reconnectionFuture;
    private ScheduledFuture<?> errorReconnectionFuture;

    // various listeners.
    private final ConnectionErrorListener errorListener = new ConnectionErrorListener();
    private final PingFailedListener failedPingListener;

    private final List<Consumer<Void>> reconnectListeners = new ArrayList<>();
    private final List<Consumer<Void>> connectListeners = new ArrayList<>();
    private final List<Consumer<Void>> errorListeners = new ArrayList<>();

    private final FortniteXMPPConfiguration configuration;

    // fortnite related things
    private Fortnite fortnite;
    private final AppType appType;
    private final PlatformType platformType;
    private DefaultFortnite.Builder fortniteBuilder;

    // connection related things
    private XMPPTCPConnection connection;
    private PingManager pingManager;
    private EntityFullJid user;
    private Account account;

    // resources
    private DefaultChatResource chatResource;
    private DefaultFriendResource friendResource;
    private DefaultPartyResource partyResource;
    private DefaultPresenceResource presenceResource;

    /**
     * Creates a new instance of {@link FortniteXMPP}
     *
     * @param builder       the builder instance used to authenticate to fortnite.
     * @param appType       the application type to use. Either {@code AppType.FORTNITE} or {@code AppType.LAUNCHER}
     * @param platformType  the type of platform
     * @param configuration the configuration to use for this instance.
     */
    DefaultFortniteXMPP(final DefaultFortnite.Builder builder,
                        final AppType appType,
                        final PlatformType platformType,
                        final FortniteXMPPConfiguration configuration) throws FortniteAuthenticationException {
        try {
            this.fortniteBuilder = builder;
            this.fortnite = builder.build();
            this.appType = appType;
            this.platformType = platformType;
            this.configuration = configuration;
        } catch (final IOException exception) {
            throw new FortniteAuthenticationException("Could not authenticate with Fortnite.", exception);
        }
        failedPingListener = () -> {
            LOGGER.atSevere().log("Ping failed, attempting reconnect.");
            renewAndReconnect();
        };
    }

    /**
     * Creates a new instance of {@link FortniteXMPP}
     *
     * @param emailAddress  the email address to use to login
     * @param password      the password to use to login
     * @param appType       the application type to use. Either {@code AppType.FORTNITE} or {@code AppType.LAUNCHER}
     * @param platformType  the type of platform
     * @param configuration the configuration to use for this instance.
     */
    DefaultFortniteXMPP(final String emailAddress,
                        final String password,
                        final AppType appType,
                        final PlatformType platformType,
                        final FortniteXMPPConfiguration configuration) throws FortniteAuthenticationException {
        try {
            this.fortniteBuilder = DefaultFortnite.Builder.newInstance(emailAddress, password);
            this.fortnite = fortniteBuilder.build();
            this.appType = appType;
            this.platformType = platformType;
            this.configuration = configuration;
        } catch (final IOException exception) {
            throw new FortniteAuthenticationException("Could not authenticate with Fortnite.", exception);
        }
        failedPingListener = () -> {
            LOGGER.atSevere().log("Ping failed, attempting reconnect.");
            renewAndReconnect();
        };
    }

    @Override
    public void connect() throws XMPPAuthenticationException {
        try {
            fortnite.account().findOneBySessionAccountId().ifPresent(session -> this.account = session);
            final var accessToken = fortnite.session().accessToken();

            // generate a unique hex ID for resource.
            final var hex = new char[]{'A', 'B', 'C', 'D', 'E', 'F', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
            final var hexId = RandomStringUtils.random(32, 0, 0, true, true, hex);
            final var resource = "V2:" + appType.getName() + ":" + platformType.name() + "::" + hexId;
            final var loadRoster = configuration.doLoadRoster();

            if (!loadRoster) {
                Roster.setRosterLoadedAtLoginDefault(false);
                Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
                Logging.logInfoIfApplicable(LOGGER.atInfo(), configuration.doEnableLogging(), "Roster loading is off.");
            }

            Logging.logInfoIfApplicable(LOGGER.atInfo(), configuration.doEnableLogging(), "Resource ID is: " + resource);
            Logging.logInfoIfApplicable(LOGGER.atInfo(), configuration.doEnableLogging(), "Initializing XMPPTCPConnection.");

            connection = new XMPPTCPConnection(XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(account.accountId(), accessToken)
                    .setXmppDomain(SERVICE_DOMAIN)
                    .setHost(SERVICE_HOST)
                    .setPort(SERVICE_PORT)
                    .setResource(resource)
                    .build());

            // removes the listener if its already added
            connection.removeConnectionListener(errorListener);
            connection.addConnectionListener(errorListener);

            connection.connect().login();
            this.user = connection.getUser();
            // initialize or re-initialize resources
            initializeResources();

            // load roster
            loadRosterIfAppropriate();
            // register ping thread
            initializeOrDisposePings(false);

            if (configuration.doKeepAlive()) keepConnectionAlive();
            connectListeners.forEach(consumer -> consumer.accept(null));
            LOGGER.atInfo().log("Connected to the XMPP service successfully.");
        } catch (final IOException | SmackException | XMPPException | InterruptedException exception) {
            throw new XMPPAuthenticationException("Could not connect to the XMPP service.", exception);
        }
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
    public CompletableFuture<FortniteXMPP> connectAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                connect();
            } catch (final XMPPAuthenticationException exception) {
                throw new CompletionException(exception);
            }
            return this;
        });
    }

    @Override
    public void disconnect() {
        fortnite.close();
        chatResource.close();
        friendResource.close();
        partyResource.close();
        presenceResource.close();

        reconnectListeners.clear();
        connectListeners.clear();

        initializeOrDisposePings(true);
        connection.disconnect();

        chatResource = null;
        friendResource = null;
        partyResource = null;
        presenceResource = null;
    }

    /**
     * Loads the roster is appropriate, as in roster loading is enabled.
     *
     * @throws SmackException.NotConnectedException if the connection is not connected
     * @throws SmackException.NotLoggedInException  if the connection is not logged in
     * @throws InterruptedException                 if there was interruption while loading or reloading.
     */
    private void loadRosterIfAppropriate() throws SmackException.NotConnectedException, SmackException.NotLoggedInException, InterruptedException {
        if (configuration.doLoadRoster()) {
            final var roster = Roster.getInstanceFor(connection);
            if (!roster.isLoaded()) roster.reloadAndWait();
            Logging.logInfoIfApplicable(LOGGER.atInfo(), configuration.doEnableLogging(), "Roster loaded.");
        }
    }

    /**
     * Initializes or disposes of the ping manager.
     */
    private void initializeOrDisposePings(final boolean dispose) {

        if (dispose) {
            pingManager.setPingInterval(-1);
            pingManager.unregisterPingFailedListener(failedPingListener);
            pingManager = null;
        } else {
            pingManager = PingManager.getInstanceFor(connection);
            pingManager.setPingInterval(60);

            pingManager.registerPingFailedListener(failedPingListener);
        }
    }

    /**
     * Initializes or re-initializes the resources.
     */
    private void initializeResources() {
        if (chatResource == null) {
            chatResource = new DefaultChatResource(this);
        } else {
            chatResource.reinitialize(this);
        }

        if (friendResource == null) {
            friendResource = new DefaultFriendResource(this, configuration.doEnableLogging());
        } else {
            friendResource.reinitialize(this);
        }

        if (partyResource == null) {
            partyResource = new DefaultPartyResource(this, configuration.doEnableLogging());
        } else {
            partyResource.reinitialize(this);
        }

        if (presenceResource == null) {
            presenceResource = new DefaultPresenceResource(this);
        } else {
            presenceResource.reinitialize(this);
        }
    }

    /**
     * Keeps the connection alive.
     */
    private void keepConnectionAlive() {
        if (reconnectionFuture == null) {
            reconnectionFuture = service.schedule(this::renewAndReconnect, configuration.getKeepAlivePeriod(), configuration.getTimeUnit());
        } else {
            reconnectionFuture.cancel(false);
            reconnectionFuture = service.schedule(this::renewAndReconnect, configuration.getKeepAlivePeriod(), configuration.getTimeUnit());
        }
    }

    /**
     * Authenticates with Fortnite again and then attempts to reconnect.
     */
    private void renewAndReconnect() {
        reconnectListeners.forEach(consumer -> consumer.accept(null));
        LOGGER.atInfo().log("Attempting to reconnect to the XMPP service.");
        disconnectAndDispose();
        initializeOrDisposePings(true);

        try {
            this.fortnite = fortniteBuilder.build();
            connect();
        } catch (final IOException | XMPPAuthenticationException exception) {
            LOGGER.atSevere().withCause(exception).log("Failed to reconnect, retrying in  " + configuration.getReconnectionWaitTime() + " seconds.");
            scheduleErrorReconnect();
        }
    }

    /**
     * Disconnects from the fortnite and XMPP service and then disposes of connection stuff.
     */
    private void disconnectAndDispose() {
        try {
            fortnite.close();
            chatResource.disposeConnection();
            friendResource.disposeConnection();
            partyResource.disposeConnection();
            presenceResource.disposeConnection();
            connection.disconnect();
        } catch (final Exception exception) {
            LOGGER.atWarning().withCause(exception).log("Failed to close some resources.");
        }
    }

    /**
     * An error occurred, so schedule a reconnect sometime in the future.
     */
    private void scheduleErrorReconnect() {
        if (errorReconnectionFuture == null) {
            errorReconnectionFuture = service.schedule(DefaultFortniteXMPP.this::renewAndReconnect, configuration.getReconnectionWaitTime(), configuration.getTimeUnit());
        } else {
            errorReconnectionFuture.cancel(false);
            errorReconnectionFuture = service.schedule(DefaultFortniteXMPP.this::renewAndReconnect, configuration.getReconnectionWaitTime(), configuration.getTimeUnit());
        }
    }

    @Override
    public void onReconnect(final Consumer<Void> consumer) {
        reconnectListeners.add(consumer);
    }

    @Override
    public void onConnected(final Consumer<Void> consumer) {
        connectListeners.add(consumer);
    }

    @Override
    public void onConnectionError(Consumer<Void> consumer) {
        errorListeners.add(consumer);
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

    private final class ConnectionErrorListener implements ConnectionListener {
        @Override
        public void connected(XMPPConnection connection) {
            Logging.logInfoIfApplicable(LOGGER.atInfo(), configuration.doEnableLogging(), "Connection established.");
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            Logging.logInfoIfApplicable(LOGGER.atInfo(), configuration.doEnableLogging(), "Connection authenticated.");
        }

        @Override
        public void connectionClosed() {
            Logging.logInfoIfApplicable(LOGGER.atInfo(), configuration.doEnableLogging(), "Connection was closed.");
        }

        @Override
        public void connectionClosedOnError(Exception exception) {
            exception.printStackTrace();
            errorListeners.forEach(errorListener -> errorListener.accept(null));

            if (configuration.doReconnectOnError()) {
                LOGGER.atInfo().log("Attempting to reconnect in: " + configuration.getReconnectionWaitTime() + " seconds.");
                scheduleErrorReconnect();
            }
        }
    }
}
