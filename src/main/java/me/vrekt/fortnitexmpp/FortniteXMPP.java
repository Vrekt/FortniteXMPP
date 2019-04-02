package me.vrekt.fortnitexmpp;

import io.github.robertograham.fortnite2.client.Fortnite;
import me.vrekt.fortnitexmpp.chat.ChatResource;
import me.vrekt.fortnitexmpp.exception.FortniteAuthenticationException;
import me.vrekt.fortnitexmpp.exception.XMPPAuthenticationException;
import me.vrekt.fortnitexmpp.friend.FriendResource;
import me.vrekt.fortnitexmpp.party.PartyResource;
import me.vrekt.fortnitexmpp.presence.PresenceResource;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.EntityFullJid;

import java.util.function.Consumer;

public interface FortniteXMPP {

    String SERVICE_DOMAIN = "prod.ol.epicgames.com";
    String SERVICE_HOST = "xmpp-service-prod.ol.epicgames.com";
    int SERVICE_PORT = 5222;

    /**
     * Create a new {@link FortniteXMPP} instance.
     *
     * @param fortnite     the already authenticated {@link Fortnite} instance
     * @param appType      the application type to use. Either {@code AppType.FORTNITE} or {@code AppType.LAUNCHER}
     * @param platformType the type of platform
     * @return a new {@link FortniteXMPP} instance.
     */
    static FortniteXMPP newFortniteXMPP(final Fortnite fortnite, AppType appType, final PlatformType platformType) {
        return new DefaultFortniteXMPP(fortnite, appType, platformType);
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
    static FortniteXMPP newFortniteXMPP(final String emailAddress, final String password, final AppType appType, final PlatformType platformType) throws FortniteAuthenticationException {
        return new DefaultFortniteXMPP(emailAddress, password, appType, platformType);
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
    void connectAsync(Consumer<Boolean> callback);

    /**
     * Disconnects from the XMPP service and disposes of everything created.
     * This should always be called on shutdown or else your connection will be spammed
     * with packets next time you connect.
     */
    void disconnect();

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

    enum PlatformType {
        WIN, MAC, IOS, AND, PSN, XBL, SWT
    }

}
