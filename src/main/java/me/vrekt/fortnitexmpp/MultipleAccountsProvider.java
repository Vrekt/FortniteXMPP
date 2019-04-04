package me.vrekt.fortnitexmpp;

import com.google.common.flogger.FluentLogger;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite;
import me.vrekt.fortnitexmpp.exception.FortniteAuthenticationException;
import me.vrekt.fortnitexmpp.exception.XMPPAuthenticationException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class MultipleAccountsProvider {
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private final Map<DefaultFortnite.Builder, ResourceProviderPair> temporaryInstances = new HashMap<>();
    private final Map<String, FortniteXMPP> authenticatedInstances = new HashMap<>();
    private final BasicXMPPConfiguration configuration;

    /**
     * Initialize this instance
     *
     * @param configuration the configuration to use for all accounts
     */
    public MultipleAccountsProvider(final BasicXMPPConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Adds one account
     *
     * @param emailAddress the email address to use to login
     * @param password     the password to use to login
     * @param appType      the application type to use. Either {@code AppType.FORTNITE} or {@code AppType.LAUNCHER}
     * @param platformType the type of platform
     * @return the current instance
     */
    public MultipleAccountsProvider addOneAccount(final String emailAddress,
                                                  final String password,
                                                  final FortniteXMPP.AppType appType,
                                                  final FortniteXMPP.PlatformType platformType) {
        temporaryInstances.put(DefaultFortnite.Builder.newInstance(emailAddress, password), new ResourceProviderPair(appType, platformType));
        return this;
    }

    /**
     * Adds one account
     *
     * @param builder      the builder
     * @param appType      the application type to use for the provided account
     * @param platformType the platform type to use for the provided account
     * @return the current instance
     */
    public MultipleAccountsProvider addOneAccount(final DefaultFortnite.Builder builder,
                                                  final FortniteXMPP.AppType appType,
                                                  final FortniteXMPP.PlatformType platformType) {
        temporaryInstances.put(builder, new ResourceProviderPair(appType, platformType));
        return this;
    }

    /**
     * Adds many accounts
     *
     * @param appType        the application type to use for all provided accounts
     * @param platformType   the platform type to use for all provided accounts
     * @param credentialPair the pair of credentials. This must follow the following pattern:
     *                       addMany(app, platform, username, password, username, password)
     *                       addMany(app, platform, username, password)
     * @return the current instance
     */
    public MultipleAccountsProvider addMany(final FortniteXMPP.AppType appType,
                                            final FortniteXMPP.PlatformType platformType,
                                            final String... credentialPair) {
        for (int i = 0; i <= credentialPair.length; i += 2) {
            if (i + 1 > credentialPair.length - 1) break;
            temporaryInstances.put(DefaultFortnite.Builder.newInstance(credentialPair[i], credentialPair[i + 1]), new ResourceProviderPair(appType, platformType));
        }
        return this;
    }

    /**
     * Adds many accounts
     *
     * @param appType      the application type to use for all provided accounts
     * @param platformType the platform type to use for all provided accounts
     * @param builders     the array of builders
     * @return the current instance
     */
    public MultipleAccountsProvider addMany(final FortniteXMPP.AppType appType,
                                            final FortniteXMPP.PlatformType platformType,
                                            final DefaultFortnite.Builder... builders) {
        for (DefaultFortnite.Builder builder : builders) temporaryInstances.put(builder, new ResourceProviderPair(appType, platformType));
        return this;
    }

    /**
     * Attempt to get the {@link FortniteXMPP} by the display name.
     *
     * @param displayName the display name of the account
     * @return the account with the provided {@code displayName} or {@code null} if no account was found.
     */
    public FortniteXMPP getByDisplayName(final String displayName) {
        return authenticatedInstances.get(displayName);
    }

    /**
     * Attempt to get the {@link FortniteXMPP} by the account ID.
     *
     * @param accountId the ID of the account
     * @return the account with the provided {@code accountId} or {@code null} if no account was found.
     */
    public FortniteXMPP getByAccountId(final String accountId) {
        return authenticatedInstances.values().stream().filter(fortniteXMPP -> fortniteXMPP.accountId().equals(accountId)).findAny().orElse(null);
    }

    /**
     * Attempts to connect all accounts provided.
     * If a {@link FortniteAuthenticationException} or {@link XMPPAuthenticationException} occur,
     * then the exception is logged.
     */
    public void connectAll() {
        final var iterator = temporaryInstances.entrySet().iterator();
        while (iterator.hasNext()) {
            final var entry = iterator.next();
            final var builder = entry.getKey();
            final var resourceProviderPair = entry.getValue();

            try {
                final var instance = FortniteXMPP.newFortniteXMPP(builder, resourceProviderPair.appType, resourceProviderPair.platformType);
                instance.logExceptionsAndWarnings(configuration.logExceptions);
                instance.setLoadRoster(configuration.loadRoster);
                instance.connect();

                authenticatedInstances.put(instance.displayName(), instance);
            } catch (final FortniteAuthenticationException | XMPPAuthenticationException exception) {
                LOGGER.atWarning().log("Failed to connect one account! Message: " + exception.getMessage());
            }
            iterator.remove();
        }
    }

    /**
     * Attempts to connect all accounts provided async.
     *
     * @param callback the callback that is invoked when all accounts are connected. If {@code true} is consumed,
     *                 this indicates all accounts connected successfully. If {@code false} is consumed,
     *                 this indicates one or more accounts failed to authenticate or connect.
     */
    public void connectAllAsync(final Consumer<Boolean> callback) {
        final var result = new AtomicBoolean(true);
        CompletableFuture.runAsync(() -> {
            final var iterator = temporaryInstances.entrySet().iterator();
            final var latch = new CountDownLatch(temporaryInstances.entrySet().size());
            // iterate through and connect each account invoking connectAsync
            while (iterator.hasNext()) {
                final var entry = iterator.next();
                final var builder = entry.getKey();
                final var resourceProviderPair = entry.getValue();

                try {
                    final var instance = FortniteXMPP.newFortniteXMPP(builder, resourceProviderPair.appType, resourceProviderPair.platformType);
                    instance.setLoadRoster(configuration.loadRoster);
                    instance.connectAsync(c -> {
                        instance.logExceptionsAndWarnings(configuration.logExceptions);
                        // only set the result if its already true.
                        // we don't want to have one account fail, another succeed and have the result true.
                        if (result.get()) result.set(c);
                        latch.countDown();

                        // add the authenticated instance.
                        authenticatedInstances.put(instance.displayName(), instance);
                    });
                } catch (final FortniteAuthenticationException exception) {
                    LOGGER.atWarning().log("Failed to connect one account! Message: " + exception.getMessage());
                }
                iterator.remove();
            }

            try {
                latch.await();
            } catch (final InterruptedException exception) {
                exception.printStackTrace();
            }

            // accept the result
            callback.accept(result.get());
        });
    }

    /**
     * Used to store app/platform types
     */
    private final class ResourceProviderPair {

        private final FortniteXMPP.AppType appType;
        private final FortniteXMPP.PlatformType platformType;

        ResourceProviderPair(final FortniteXMPP.AppType appType, final FortniteXMPP.PlatformType platformType) {
            this.appType = appType;
            this.platformType = platformType;
        }

    }

    /**
     * The configuration to use for all accounts
     */
    public static final class BasicXMPPConfiguration {
        private final boolean logExceptions, loadRoster;

        public BasicXMPPConfiguration(boolean logExceptions, boolean loadRoster) {
            this.logExceptions = logExceptions;
            this.loadRoster = loadRoster;
        }
    }

}
