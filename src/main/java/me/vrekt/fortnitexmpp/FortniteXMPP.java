package me.vrekt.fortnitexmpp;

import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite;
import me.vrekt.fortnitexmpp.chat.ChatResource;
import me.vrekt.fortnitexmpp.exception.FortniteAuthenticationException;
import me.vrekt.fortnitexmpp.exception.XMPPAuthenticationException;
import me.vrekt.fortnitexmpp.friend.FriendResource;
import me.vrekt.fortnitexmpp.party.PartyResource;
import me.vrekt.fortnitexmpp.presence.PresenceResource;
import me.vrekt.fortnitexmpp.provider.FortniteXMPPConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.EntityFullJid;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface FortniteXMPP {

    String SERVICE_DOMAIN = "prod.ol.epicgames.com";
    String SERVICE_HOST = "xmpp-service-prod.ol.epicgames.com";
    int SERVICE_PORT = 5222;

    /**
     * Create a new {@link FortniteXMPP} instance.
     *
     * @param builder      the builder instance used to authenticate to fortnite.
     * @param appType      the application type to use. Either {@code AppType.FORTNITE} or {@code AppType.LAUNCHER}
     * @param platformType the type of platform
     * @return a new {@link FortniteXMPP} instance.
     */
    static FortniteXMPP newFortniteXMPP(final DefaultFortnite.Builder builder,
                                        final AppType appType,
                                        final PlatformType platformType,
                                        final FortniteXMPPConfiguration configuration) throws FortniteAuthenticationException {
        return new DefaultFortniteXMPP(builder, appType, platformType, configuration);
    }

    /**
     * Creates a new {@link FortniteXMPP} instance.
     *
     * @param emailAddress the email address to use to login
     * @param password     the password to use to login
     * @param appType      the application type to use. Either {@code AppType.FORTNITE} or {@code AppType.LAUNCHER}
     * @param platformType the type of platform
     * @return a new {@link FortniteXMPP} instance.
     */
    static FortniteXMPP newFortniteXMPP(final String emailAddress,
                                        final String password,
                                        final AppType appType,
                                        final PlatformType platformType,
                                        final FortniteXMPPConfiguration configuration) throws FortniteAuthenticationException {
        return new DefaultFortniteXMPP(emailAddress, password, appType, platformType, configuration);
    }

    /**
     * Attempts to connect the XMPP service.
     *
     * @throws XMPPAuthenticationException if an error occurred while trying to connect.
     */
    void connect() throws XMPPAuthenticationException;

    /**
     * Attempts to connect to the XMPP service async, accepting {@code true} if successful.
     *
     * @param callback the callback
     */
    void connectAsync(final Consumer<Boolean> callback);

    /**
     * Attempts to connect to the XMPP service async.
     *
     * @return an instance of {@link FortniteXMPP} wrapped in {@link CompletableFuture}
     */
    CompletableFuture<FortniteXMPP> connectAsync();

    /**
     * Disconnects from the XMPP service and disposes of everything created.
     * This should always be called on shutdown or else your connection will be spammed
     * with packets next time you connect.
     */
    void disconnect();

    /**
     * Adds a reconnect listener, consumes {@code null} when a reconnection is happening.
     *
     * @param consumer the consumer
     */
    void onReconnect(final Consumer<Void> consumer);

    /**
     * Adds a connected listener, consumes {@code null} when a successful connection is established.
     *
     * @param consumer the consumer
     */
    void onConnected(final Consumer<Void> consumer);

    /**
     * Adds a connection error listeners, consumes {@code null} when the connection encounters a stream error and disconnects.
     *
     * @param consumer the consumer
     */
    void onConnectionError(final Consumer<Void> consumer);

    /**
     * @return the {@link Fortnite} instance created or provided.
     */
    Fortnite fortnite();

    /**
     * @return the account ID of the current authenticated account.
     */
    String accountId();

    /**
     * @return the display name of the current authenticated account.
     */
    String displayName();

    /**
     * @return the internal {@link XMPPTCPConnection}
     */
    XMPPTCPConnection connection();

    /**
     * @return the {@link EntityFullJid} of the current authenticated account.
     */
    EntityFullJid user();

    /**
     * @return the internal {@link ChatResource} instance used for sending and receiving messages.
     */
    ChatResource chat();

    /**
     * @return the internal {@link FriendResource} instance used for friend related actions.
     */
    FriendResource friend();

    /**
     * @return the internal {@link PartyResource} instance used for party related actions.
     */
    PartyResource party();

    /**
     * @return the internal {@link PresenceResource} instance used for presence related actions.
     */
    PresenceResource presence();

    /**
     * Valid types for the XMPP service.
     */
    enum AppType {
        FORTNITE("Fortnite"), LAUNCHER("launcher");

        private final String name;

        AppType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Valid platforms Fortnite can be played on.
     */
    enum PlatformType {
        WIN, MAC, IOS, AND, PSN, XBL, SWT
    }

}
