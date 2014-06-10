package com.tradier.raven.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.base.Optional;
import org.slf4j.LoggerFactory;

/**
 * A class adding a configured {@link net.kencochrane.raven.logback.SentryAppender} to the root logger.
 */
public final class RavenBootstrap {
    private RavenBootstrap() { /* No instance methods */ }

    /**
     * Bootstrap the SLF4J root logger with a configured {@link net.kencochrane.raven.logback.SentryAppender}.
     *
     * @param dsn             The DSN (Data Source Name) for your project
     * @param cleanRootLogger If true, detach and stop all other appenders from the root logger
     */
    public static void bootstrap(final String dsn, boolean cleanRootLogger) {
        bootstrap(dsn, Optional.<String>absent(), cleanRootLogger);
    }

    /**
     * Bootstrap the SLF4J root logger with a configured {@link net.kencochrane.raven.logback.SentryAppender}.
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

        final Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

        if (cleanRootLogger) {
            root.detachAndStopAllAppenders();
        }

        root.addAppender(raven.build(root.getLoggerContext(), dsn, null));
    }
}
