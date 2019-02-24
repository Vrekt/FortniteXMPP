package me.vrekt.fortnitexmpp.utility;

import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite;
import me.vrekt.fortnitexmpp.FortniteXmpp;
import me.vrekt.fortnitexmpp.implementation.AppType;
import me.vrekt.fortnitexmpp.implementation.PlatformType;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class used for handling multiple bot accounts.
 */
public final class MultipleAccountsProvider {

    private final List<TemporaryAccountProvider> temporaryAccountProviders = new ArrayList<>();
    private final Map<String, FortniteXmpp> accounts = new HashMap<>();

    /**
     * Add an account to be logged in later
     *
     * @param username the username
     * @param password the password
     * @param id       the ID of the account, used for getting it later.
     */
    public void addAccount(String username, String password, String id) {
        temporaryAccountProviders.add(new TemporaryAccountProvider(username, password, id));
    }

    /**
     * Disconnects every account
     */
    public void disconnectAll() {
        accounts.values().forEach(FortniteXmpp::disconnect);
        accounts.clear();
    }

    /**
     * Connects all accounts
     *
     * @param appType      the application {@link AppType} "Fortnite" "launcher"
     * @param platformType the platform {@link PlatformType}
     */
    public void connectAll(AppType appType, PlatformType platformType) {
        temporaryAccountProviders.forEach(provider -> connectOrThrow(provider, appType, platformType));
    }

    /**
     * Connects one account
     *
     * @param id           the ID of the account, not to be confused with an actual epic games account ID.
     * @param appType      the application {@link AppType} "Fortnite" "launcher"
     * @param platformType the platform {@link PlatformType}
     * @return a new {@link FortniteXmpp} instance if connected successfully
     */
    public FortniteXmpp connectOne(String id, AppType appType, PlatformType platformType) {
        var provider = temporaryAccountProviders.stream().filter(p -> p.id.equals(id)).findFirst().orElse(null);
        if (provider == null) throw new IllegalArgumentException("ID not found!");
        return connectOrThrow(provider, appType, platformType);
    }

    /**
     * Attempts to connect an account to the XMPP service, throwing an exception if failing.
     *
     * @param provider     the account provider
     * @param appType      the application {@link AppType} "Fortnite" "launcher"
     * @param platformType the platform {@link PlatformType}
     * @return {@code null} IF an exception occurred.
     */
    private FortniteXmpp connectOrThrow(TemporaryAccountProvider provider, AppType appType, PlatformType platformType) {
        try {
            System.out.println("Connecting account: " + provider.id);
            var service = FortniteXmpp.newBuilder(provider.build()).setApplication(appType).setPlatform(platformType).build();
            service.connect();

            System.out.println(service.getAccountId() + ":" + service.getDisplayName() + " has connected!");
            accounts.put(provider.id, service);
            return service;
        } catch (IOException | SmackException | XMPPException | InterruptedException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * @return a list of connected accounts
     */
    public Map<String, FortniteXmpp> getAccounts() {
        return accounts;
    }

    /**
     * @return how many accounts were provided
     */
    public int getAccountsSize() {
        return accounts.values().size();
    }

    /**
     * @param id the ID of the account, (Not account ID)
     * @return an account with the ID
     */
    public FortniteXmpp getById(String id) {
        return accounts.get(id);
    }

    private class TemporaryAccountProvider {

        private final String username, password, id;

        TemporaryAccountProvider(String username, String password, String id) {
            this.username = username;
            this.password = password;
            this.id = id;
        }

        public Fortnite build() throws IOException {
            return DefaultFortnite.Builder.newInstance(username, password).setKillOtherSessions(true).build();
        }
    }

}
