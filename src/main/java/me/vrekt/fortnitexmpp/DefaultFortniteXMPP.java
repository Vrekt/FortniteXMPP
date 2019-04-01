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
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingManager;
import org.jxmpp.jid.EntityFullJid;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class DefaultFortniteXMPP implements FortniteXMPP {

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private final Fortnite fortnite;
    private final AppType appType;
    private final PlatformType platformType;

    private XMPPTCPConnection connection;
    private EntityFullJid user;
    private Account account;

    private DefaultChatResource chatResource;
    private DefaultFriendResource friendResource;
    private DefaultPartyResource partyResource;

    /**
     * Creates a new instance of {@link FortniteXMPP}
     *
     * @param fortnite     the already authenticated {@link Fortnite} instance
     * @param appType      the application type to use. Either {@code AppType.FORTNITE} or {@code AppType.LAUNCHER}
     * @param platformType the type of platform
     */
    DefaultFortniteXMPP(final Fortnite fortnite, final AppType appType, final PlatformType platformType) {
        this.fortnite = fortnite;
        this.appType = appType;
        this.platformType = platformType;
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
            this.fortnite = DefaultFortnite.Builder.newInstance(emailAddress, password).build();
            this.appType = appType;
            this.platformType = platformType;
        } catch (final IOException exception) {
            throw new FortniteAuthenticationException("Could not authenticate with Fortnite.", exception);
        }
    }

    @Override
    public void connect() throws XMPPAuthenticationException {
        try {
            fortnite.account().findOneBySessionAccountId().ifPresent(acc -> this.account = acc);
            final var accessToken = fortnite.session().accessToken();

            connection = new XMPPTCPConnection(XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(account.accountId(), accessToken)
                    .setXmppDomain(SERVICE_DOMAIN)
                    .setHost(SERVICE_HOST)
                    .setPort(SERVICE_PORT)
                    .setResource("V2:" + appType.getName() + ":" + platformType.name())
                    .setConnectTimeout(60000)
                    .build());

            final var roster = Roster.getInstanceFor(connection);
            roster.setRosterLoadedAtLogin(false);

            connection.connect().login();
            connection.setReplyTimeout(120000);
            this.user = connection.getUser();

            // attempt to load the roster.
            if (!roster.isLoaded()) roster.reloadAndWait();

            // set the ping interval, makes a more stable connection.
            final var pingManager = PingManager.getInstanceFor(connection);
            pingManager.setPingInterval(60);

            chatResource = new DefaultChatResource(connection);
            friendResource = new DefaultFriendResource(this);
            partyResource = new DefaultPartyResource(this);
            LOGGER.atInfo().log("Finished loading!");
        } catch (final IOException | SmackException | XMPPException | InterruptedException exception) {
            throw new XMPPAuthenticationException("Could not connect to the XMPP service.", exception);
        }

    }

    @Override
    public void connectAsync(Consumer<Boolean> callback) {
        CompletableFuture.runAsync(() -> {
            try {
                connect();
                callback.accept(true);
            } catch (final XMPPAuthenticationException exception) {
                callback.accept(false);
            }
        });
    }

    @Override
    public void disconnect() {
        fortnite.close();
        chatResource.close();
        friendResource.close();
        partyResource.close();
        connection.disconnect();
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
}
