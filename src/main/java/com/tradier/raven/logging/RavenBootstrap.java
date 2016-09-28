package com.tradier.raven.logging;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;
import io.dropwizard.logging.async.AsyncLoggingEventAppenderFactory;
import io.dropwizard.logging.filter.ThresholdLevelFilterFactory;
import io.dropwizard.logging.layout.DropwizardLayoutFactory;

import java.util.Optional;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * A class adding a configured {@link com.getsentry.raven.logback.SentryAppender} to the root
 * logger.
 */
public final class RavenBootstrap {
  private RavenBootstrap() { /* No instance methods */ }

  /**
   * Bootstrap the SLF4J root logger with a configured
   * {@link com.getsentry.raven.logback.SentryAppender}.
   *
   * @param dsn The DSN (Data Source Name) for your project
   */
  public static void bootstrap(final String dsn) {
    bootstrap(dsn, true);
  }

  /**
   * Bootstrap the SLF4J root logger with a configured
   * {@link com.getsentry.raven.logback.SentryAppender}.
   *
   * @param dsn The DSN (Data Source Name) for your project
   * @param cleanRootLogger If true, detach and stop all other appenders from the root logger
   */
  public static void bootstrap(final String dsn, boolean cleanRootLogger) {
    bootstrap(dsn, Optional.empty(), cleanRootLogger);
  }

  /**
   * Bootstrap the SLF4J root logger with a configured
   * {@link com.getsentry.raven.logback.SentryAppender}.
   *
   * @param dsn The DSN (Data Source Name) for your project
   * @param tags Custom tags to send to Sentry along with errors
   * @param cleanRootLogger If true, detach and stop all other appenders from the root logger
   */
  public static void bootstrap(final String dsn, Optional<String> tags, boolean cleanRootLogger) {
    bootstrap(dsn, tags, Optional.empty(), Optional.empty(), cleanRootLogger);
  }

  /**
   * Bootstrap the SLF4J root logger with a configured
   * {@link com.getsentry.raven.logback.SentryAppender}.
   *
   * @param dsn The DSN (Data Source Name) for your project
   * @param tags Custom tags to send to Sentry along with errors
   * @param environment The environment name to pass to Sentry
   * @param release The release name to pass to Sentry
   * @param cleanRootLogger If true, detach and stop all other appenders from the root logger
   */
  public static void bootstrap(final String dsn, Optional<String> tags,
      Optional<String> environment, Optional<String> release, boolean cleanRootLogger) {
    bootstrap(dsn, tags, environment, release, Optional.empty(), cleanRootLogger);
  }

  /**
   * Bootstrap the SLF4J root logger with a configured
   * {@link com.getsentry.raven.logback.SentryAppender}.
   *
   * @param dsn The DSN (Data Source Name) for your project
   * @param tags Custom tags to send to Sentry along with errors
   * @param environment The environment name to pass to Sentry
   * @param release The release name to pass to Sentry
   * @param serverName The server name to pass to Sentry
   * @param cleanRootLogger If true, detach and stop all other appenders from the root logger
   */
  public static void bootstrap(final String dsn, Optional<String> tags,
      Optional<String> environment, Optional<String> release, Optional<String> serverName,
      boolean cleanRootLogger) {
    final RavenAppenderFactory raven = new RavenAppenderFactory();
    raven.setThreshold(Level.ERROR);
    raven.setDsn(dsn);
    raven.setTags(tags);
    raven.setEnvironment(environment);
    raven.setRelease(release);
    raven.setServerName(serverName);

    registerAppender(dsn, cleanRootLogger, raven);
  }

  private static void registerAppender(String dsn, boolean cleanRootLogger,
      RavenAppenderFactory raven) {
    final Logger root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);

    if (cleanRootLogger) {
      root.detachAndStopAllAppenders();
    }

    final ThresholdLevelFilterFactory levelFilterFactory = new ThresholdLevelFilterFactory();
    final DropwizardLayoutFactory layoutFactory = new DropwizardLayoutFactory();
    final AsyncLoggingEventAppenderFactory asyncAppenderFactory =
        new AsyncLoggingEventAppenderFactory();
    root.addAppender(raven.build(root.getLoggerContext(), dsn, layoutFactory, levelFilterFactory,
        asyncAppenderFactory));
  }
}
