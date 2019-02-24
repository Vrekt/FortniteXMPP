package me.vrekt.fortnitexmpp;

import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.domain.Account;
import me.vrekt.fortnitexmpp.implementation.AppType;
import me.vrekt.fortnitexmpp.implementation.PlatformType;
import me.vrekt.fortnitexmpp.implementation.service.chat.ChatService;
import me.vrekt.fortnitexmpp.implementation.service.chat.DefaultChatService;
import me.vrekt.fortnitexmpp.implementation.service.friend.DefaultFriendService;
import me.vrekt.fortnitexmpp.implementation.service.friend.FriendService;
import me.vrekt.fortnitexmpp.implementation.service.party.DefaultPartyService;
import me.vrekt.fortnitexmpp.implementation.service.party.PartyService;
import me.vrekt.fortnitexmpp.implementation.service.presence.DefaultPresenceService;
import me.vrekt.fortnitexmpp.implementation.service.presence.PresenceService;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingManager;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;

import java.io.IOException;

public final class DefaultFortniteXmpp implements FortniteXmpp {

    private final String serviceDomain = "prod.ol.epicgames.com";
    private final String serviceHostDomain = "xmpp-service-prod.ol.epicgames.com";
    private final int servicePort = 5222;

    private final Fortnite fortnite;
    private final AppType appType;
    private final PlatformType platformType;

    private Account account;
    private XMPPTCPConnection connection;
    private Jid jid;

    private DefaultPartyService partyService;
    private DefaultPresenceService presenceService;
    private DefaultFriendService friendService;
    private DefaultChatService chatService;

    DefaultFortniteXmpp(Fortnite fortnite, AppType appType, PlatformType platformType) {
        this.fortnite = fortnite;
        this.appType = appType;
        this.platformType = platformType;
    }

    @Override
    public void connect() throws IOException, SmackException, XMPPException, InterruptedException {
        fortnite.account().findOneBySessionAccountId().ifPresent(account1 -> this.account = account1);
        var token = fortnite.session().accessToken();

        connection = new XMPPTCPConnection(XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(account.accountId(), token)
                .setXmppDomain(serviceDomain)
                .setHost(serviceHostDomain)
                .setPort(servicePort)
                .build());

        final var resource = Resourcepart.fromOrThrowUnchecked("V2:" + appType.getName() + ":" + platformType.name());
        connection.connect().login(account.accountId(), token, resource);
        this.jid = JidCreate.from(account.accountId(), connection.getXMPPServiceDomain(), resource);

        final var pingManager = PingManager.getInstanceFor(connection);
        pingManager.setPingInterval(120);

        System.out.println("Connected to XMPP service successfully!");
        partyService = new DefaultPartyService(this);
        presenceService = new DefaultPresenceService(connection);
        friendService = new DefaultFriendService(this);
        chatService = new DefaultChatService(connection);
    }

    @Override
    public void disconnect() {
        partyService.close();
        presenceService.close();
        friendService.close();
        chatService.close();

        PingManager.getInstanceFor(connection).setPingInterval(-1);
        connection.disconnect();
    }

    @Override
    public XMPPTCPConnection getConnection() {
        return connection;
    }

    @Override
    public Fortnite getFortnite() {
        return fortnite;
    }

    @Override
    public String getAccountId() {
        return account.accountId();
    }

    @Override
    public String getDisplayName() {
        return account.displayName();
    }

    @Override
    public Jid getJid() {
        return jid;
    }

    @Override
    public PartyService getPartyService() {
        return partyService;
    }

    @Override
    public PresenceService getPresenceService() {
        return presenceService;
    }

    @Override
    public FriendService getFriendService() {
        return friendService;
    }

    @Override
    public ChatService getChatService() {
        return chatService;
    }
}
