# Dropwizard Raven

Dropwizard integration for error logging to [Sentry](https://getsentry.com).

## Installation

This project is available on Maven Central. To add it to your project simply add the following dependency to your POM:

    <dependency>
      <groupId>com.tradier</groupId>
      <artifactId>dropwizard-raven</artifactId>
      <version>0.0.1</version>
    </dependency>

## Usage

The Dropwizard Raven provides an `AppenderFactory` which is automatically registered in Dropwizard and will send error logs to Sentry.

### Configuration

The Logback `SentryAppender` can be configured using the provided `RavenConfiguration` class which basically mirrors the
appender configuration outlined in [raven-logback](https://github.com/getsentry/raven-java/tree/master/raven-logback).

Include the `raven` appender in your `config.yml`:

    appenders:
      - type: console
      - type: raven
        dsn: https://user:pass@getsentry.com/appid
        tags: tag:value,tag2:value


### Logging startup errors

In order to log startup errors (i.e. before the `RavenAppenderFactory` has been properly initialized), the Dropwizard application has to run the `RavenBootstrap.bootstrap()` in its `main` method and set a custom `UncaughtExceptionHandler` for the main thread.

    public static void main(String[] args) throws Exception {
        RavenBootstrap.bootstrap(DSN, TAGS, false);
        Thread.currentThread().setUncaughtExceptionHandler(UncaughtExceptionHandlers.systemExit());

        new MyDropwizardApplication().run(args);
    }

## Support

Please file bug reports and feature requests in [GitHub issues](https://github.com/tradier/dropwizard-raven/issues).

## Acknowledgements

Thanks to @gini for [dropwizard-gelf](https://github.com/gini/dropwizard-gelf).

## Copyright

Copyright (c) 2014 Tradier Inc. See [LICENSE](LICENSE.md) for detail.
