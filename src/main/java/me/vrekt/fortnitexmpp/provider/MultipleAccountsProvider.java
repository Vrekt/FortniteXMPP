package me.vrekt.fortnitexmpp.provider;

import com.google.common.flogger.FluentLogger;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite;
import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.exception.FortniteAuthenticationException;
import me.vrekt.fortnitexmpp.exception.XMPPAuthenticationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * This class provides an easy way to handle multiple accounts.
 */
public final class MultipleAccountsProvider {

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private final List<AccountProvider> providers = new ArrayList<>();
    private final Map<String, FortniteXMPP> accounts = new HashMap<>();

    /**
     * Add one account
     *
     * @param configuration the configuration to use for this account
     * @param appType       the app type for this account
     * @param platformType  the platform for this account
     * @param emailAddress  the email address of this account
     * @param password      the password of this account
     * @return the current instance
     */
    public MultipleAccountsProvider addOneAccount(final FortniteXMPPConfiguration configuration,
                                                  final FortniteXMPP.AppType appType,
                                                  final FortniteXMPP.PlatformType platformType,
                                                  final String emailAddress, final String password) {
        providers.add(new AccountProvider(configuration, DefaultFortnite.Builder.newInstance(emailAddress, password), appType, platformType));
        return this;
    }

    /**
     * Add one account
     *
     * @param configuration the configuration to use for this account
     * @param appType       the app type for this account
     * @param platformType  the platform for this account
     * @param builder       the builder instance for this account
     * @return the current instance
     */
    public MultipleAccountsProvider addOneAccount(final FortniteXMPPConfiguration configuration,
                                                  final FortniteXMPP.AppType appType,
                                                  final FortniteXMPP.PlatformType platformType,
                                                  final DefaultFortnite.Builder builder) {
        providers.add(new AccountProvider(configuration, builder, appType, platformType));
        return this;
    }

    /**
     * Add many accounts
     *
     * @param configuration the configuration to use for all accounts
     * @param appType       the app type to use for all accounts
     * @param platformType  the platform type to use for all accounts
     * @param credentials   the credential pair, this should follow this pattern:
     *                      addMany(x, x, x, username, password)
     *                      addMany(x, x, x, username, password, username, password)
     *                      addMany(x, x, x, username, password, username, password, username, password)
     *                      etc...
     * @return the current instance
     */
    public MultipleAccountsProvider addManyAccounts(final FortniteXMPPConfiguration configuration,
                                                    final FortniteXMPP.AppType appType,
                                                    final FortniteXMPP.PlatformType platformType,
                                                    final String... credentials) {
        for (int i = 0; i <= credentials.length; i += 2) {
            if (i + 1 > credentials.length - 1) break;
            providers.add(new AccountProvider(configuration, DefaultFortnite.Builder.newInstance(credentials[i], credentials[i + 1]), appType, platformType));
        }
        return this;
    }

    /**
     * Add many accounts
     *
     * @param configuration the configuration to use for all accounts
     * @param appType       the app type to use for all accounts
     * @param platformType  the platform type to use for all accounts
     * @param credentials   the array of builder instances to use
     * @return the current instance
     */
    public MultipleAccountsProvider addManyAccounts(final FortniteXMPPConfiguration configuration,
                                                    final FortniteXMPP.AppType appType,
                                                    final FortniteXMPP.PlatformType platformType,
                                                    final DefaultFortnite.Builder... credentials) {
        for (DefaultFortnite.Builder credential : credentials) providers.add(new AccountProvider(configuration, credential, appType, platformType));
        return this;
    }

    /**
     * Connects all accounts, this method blocks until all accounts are connected.
     */
    public void connectAll() {
        providers.forEach(provider -> {
            try {
                final var instance = FortniteXMPP.newFortniteXMPP(provider.builderInstance, provider.appType, provider.platformType, provider.configuration);
                instance.connect();

                accounts.put(instance.displayName(), instance);
            } catch (final FortniteAuthenticationException | XMPPAuthenticationException exception) {
                LOGGER.atWarning().withCause(exception).log("Failed to connect one account!");
            }
        });
        providers.clear();
    }

    /**
     * Connects all accounts async, accepting {@code true} if all accounts successfully connected, or {@code false} otherwise.
     *
     * @param callback the callback
     */
    public void connectAllAsync(final Consumer<Boolean> callback) {
        final var result = new AtomicBoolean(true);
        final var latch = new CountDownLatch(providers.size());

        CompletableFuture.runAsync(() -> {
            providers.forEach(provider -> authenticateAsync(provider).exceptionally(throwable -> {
                LOGGER.atWarning().withCause(throwable).log("Failed to authenticate one account!");
                result.compareAndSet(true, false);
                latch.countDown();
                return null;
            }).thenCompose(FortniteXMPP::connectAsync).exceptionally(throwable -> {
                LOGGER.atWarning().withCause(throwable).log("Failed to connect one account!");
                result.compareAndSet(true, false);
                latch.countDown();
                return null;
            }).thenAccept(xmpp -> {
                accounts.put(xmpp.displayName(), xmpp);
                latch.countDown();
            }));

            try {
                latch.await();
            } catch (final InterruptedException exception) {
                exception.printStackTrace();
            }

            providers.clear();
            callback.accept(result.get());
        });
    }

    /**
     * Connects all accounts async returning a list of futures
     * that will complete when connecting is finished.
     *
     * @return a list of {@link CompletableFuture}
     */
    public List<CompletableFuture<FortniteXMPP>> connectAllAsync() {
        final var futures = new ArrayList<CompletableFuture<FortniteXMPP>>();
        providers.forEach(provider -> futures.add(authenticateAndConnectAsync(provider)));
        providers.clear();
        return futures;
    }

    /**
     * Disconnects all connected accounts
     */
    public void disconnectAll() {
        accounts.forEach((displayName, instance) -> instance.disconnect());
        accounts.clear();
    }

    /**
     * Get the account by display name
     *
     * @param displayName the display name
     * @return the account with the {@code displayName} or {@code null} if no account was found
     */
    public FortniteXMPP getByDisplayName(final String displayName) {
        return accounts.get(displayName);
    }

    /**
     * Get the account by account ID.
     *
     * @param accountId the account ID.
     * @return the account with the {@code accountId} or {@code null} if no account was found.
     */
    public FortniteXMPP getByAccountId(final String accountId) {
        return accounts.values().stream().filter(xmpp -> xmpp.accountId().equals(accountId)).findAny().orElse(null);
    }

    /**
     * Authenticates the account async
     *
     * @param provider the provider
     * @return the future representing this result
     */
    private CompletableFuture<FortniteXMPP> authenticateAsync(final AccountProvider provider) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return FortniteXMPP.newFortniteXMPP(provider.builderInstance, provider.appType, provider.platformType, provider.configuration);
            } catch (final FortniteAuthenticationException exception) {
                throw new CompletionException(exception);
            }
        });
    }

    /**
     * Authenticates and connects the account async
     *
     * @param provider the provider
     * @return the future representing the result
     */
    private CompletableFuture<FortniteXMPP> authenticateAndConnectAsync(final AccountProvider provider) {
        final var authentication = authenticateAsync(provider);
        return authentication.thenCompose(FortniteXMPP::connectAsync);
    }

    /**
     * Provides the information for one account.
     */
    private final class AccountProvider {
        private final FortniteXMPPConfiguration configuration;
        private final DefaultFortnite.Builder builderInstance;
        private final FortniteXMPP.AppType appType;
        private final FortniteXMPP.PlatformType platformType;

        AccountProvider(final FortniteXMPPConfiguration configuration,
                        final DefaultFortnite.Builder builderInstance,
                        final FortniteXMPP.AppType appType,
                        final FortniteXMPP.PlatformType platformType) {
            this.configuration = configuration;
            this.builderInstance = builderInstance;
            this.appType = appType;
            this.platformType = platformType;
        }
    }

}
