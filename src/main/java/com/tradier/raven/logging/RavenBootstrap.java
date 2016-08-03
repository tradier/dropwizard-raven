package com.tradier.raven.logging;

import com.google.common.base.Optional;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * A class adding a configured {@link com.getsentry.raven.logback.SentryAppender} to the root logger.
 */
public final class RavenBootstrap {
    private RavenBootstrap() { /* No instance methods */ }

    /**
     * Bootstrap the SLF4J root logger with a configured {@link com.getsentry.raven.logback.SentryAppender}.
     *
     * @param dsn             The DSN (Data Source Name) for your project
     * @param cleanRootLogger If true, detach and stop all other appenders from the root logger
     */
    public static void bootstrap(final String dsn, boolean cleanRootLogger) {
        bootstrap(dsn, Optional.<String>absent(), cleanRootLogger);
    }

    /**
     * Bootstrap the SLF4J root logger with a configured {@link com.getsentry.raven.logback.SentryAppender}.
     *
     * @param dsn             The DSN (Data Source Name) for your project
     * @param tags            Custom tags to send to Sentry along with errors
     * @param cleanRootLogger If true, detach and stop all other appenders from the root logger
     */
    public static void bootstrap(final String dsn, Optional<String> tags, boolean cleanRootLogger) {
        final RavenAppenderFactory raven = new RavenAppenderFactory();
        raven.setThreshold(Level.ERROR);
        raven.setDsn(dsn);
        raven.setTags(tags.get());

        registerAppender(dsn, cleanRootLogger, raven);
    }

    /**
     * Bootstrap the SLF4J root logger with a configured {@link com.getsentry.raven.logback.SentryAppender}.
     *  @param dsn             The DSN (Data Source Name) for your project
     * @param tags            Custom tags to send to Sentry along with errors
     * @param cleanRootLogger If true, detach and stop all other appenders from the root logger
     * @param environment     The environment name to pass to Sentry
     * @param release         The release name to pass to Sentry
     */
    public static void bootstrap(
        final String dsn,
        Optional<String> tags,
        boolean cleanRootLogger,
        String environment,
        String release
    ) {
        final RavenAppenderFactory raven = new RavenAppenderFactory();
        raven.setThreshold(Level.ERROR);
        raven.setDsn(dsn);
        raven.setTags(tags.get());
        raven.setEnvironment(environment);
        raven.setRelease(release);

        registerAppender(dsn, cleanRootLogger, raven);
    }

    private static void registerAppender(
        String dsn,
        boolean cleanRootLogger,
        RavenAppenderFactory raven
    ) {
        final Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

        if (cleanRootLogger) {
            root.detachAndStopAllAppenders();
        }

        root.addAppender(raven.build(root.getLoggerContext(), dsn, null));
    }
}
