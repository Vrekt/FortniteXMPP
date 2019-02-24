package me.vrekt.fortnitexmpp;

import io.github.robertograham.fortnite2.client.Fortnite;
import me.vrekt.fortnitexmpp.implementation.AppType;
import me.vrekt.fortnitexmpp.implementation.PlatformType;
import me.vrekt.fortnitexmpp.implementation.service.chat.ChatService;
import me.vrekt.fortnitexmpp.implementation.service.friend.FriendService;
import me.vrekt.fortnitexmpp.implementation.service.party.PartyService;
import me.vrekt.fortnitexmpp.implementation.service.presence.PresenceService;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.Jid;

import java.io.IOException;
import java.util.Objects;

public interface FortniteXmpp {

    /**
     * Attempts to connect to the XMPP service.
     *
     * @throws IOException
     * @throws SmackException
     * @throws XMPPException
     * @throws InterruptedException
     */
    void connect() throws IOException, SmackException, XMPPException, InterruptedException;

    /**
     * Disconnects from the XMPP service and disposes of all services.
     * This includes party, chat, friend, match, etc
     * "disposing" usually means remove all async stanza listeners and clear maps/lists/tasks.
     */
    void disconnect();

    /**
     * @return the raw {@link XMPPTCPConnection} connection
     */
    XMPPTCPConnection getConnection();

    /**
     * @return the fortnite instance
     */
    Fortnite getFortnite();

    /**
     * @return the account ID of the connected account
     */
    String getAccountId();

    /**
     * @return the display name of the connected account
     */
    String getDisplayName();

    /**
     * @return the JID of the account
     */
    Jid getJid();

    /**
     * @return the party service
     */
    PartyService getPartyService();

    /**
     * @return the presence service
     */
    PresenceService getPresenceService();

    /**
     * @return the friend service
     */
    FriendService getFriendService();

    /**
     * @return the chat service
     */
    ChatService getChatService();


    static Builder newBuilder(Fortnite fortnite) {
        return new Builder(fortnite);
    }

    final class Builder {

        private final Fortnite fortnite;
        private AppType appType;
        private PlatformType platformType;

        private Builder(Fortnite fortnite) {
            this.fortnite = fortnite;
        }

        public Builder setApplication(AppType application) {
            Objects.requireNonNull(application);
            this.appType = application;
            return this;
        }

        public Builder setPlatform(PlatformType platformType) {
            Objects.requireNonNull(platformType);
            this.platformType = platformType;
            return this;
        }

        public FortniteXmpp build() {
            return new DefaultFortniteXmpp(fortnite, appType, platformType);
        }
    }
}
