# Dropwizard Raven

[![Maven Central](https://img.shields.io/maven-central/v/com.tradier/dropwizard-raven.svg)][mavencentral] [![Build Status](https://travis-ci.org/tradier/dropwizard-raven.svg?branch=master)][travis]

[travis]: https://travis-ci.org/tradier/dropwizard-raven
[mavencentral]: http://mvnrepository.com/artifact/com.tradier/dropwizard-raven

Dropwizard integration for error logging to [Sentry](https://sentry.io).

## Usage

The Dropwizard Raven provides an `AppenderFactory` which is automatically registered in Dropwizard and will send errors to Sentry.

### Logging startup errors

In order to log startup errors (i.e. before the `RavenAppenderFactory` has been properly initialized), the Dropwizard application has to run the `RavenBootstrap.bootstrap()` in its `main` method and set a custom `UncaughtExceptionHandler` for the main thread.

```java
public static void main(String[] args) throws Exception {
    RavenBootstrap.bootstrap(DSN);
    Thread.currentThread().setUncaughtExceptionHandler(UncaughtExceptionHandlers.systemExit());

    new MyDropwizardApplication().run(args);
}
```

### Configuration

The Logback `SentryAppender` can be configured using the provided `RavenConfiguration` class which basically mirrors the appender configuration outlined in [raven-logback](https://github.com/getsentry/raven-java/tree/master/raven-logback).

Include the `raven` appender in your `config.yml`:

```yaml
appenders:
  - type: raven
    threshold: ERROR
    dsn: https://user:pass@sentry.io/appid
    environment: production
    extraTags: foo,bar,baz
    ravenFactory: com.example.RavenFactory
    release: 1.0.0
    serverName: 10.0.0.1
    tags: tag:value,tag2:value
```

### Configuration Settings

| Setting | Default | Description | Example Value |
|---|---|---|---|
| `threshold` | ALL | The log level to configure to send to Sentry | `ERROR` |
| `dsn` |   | Data Source Name - `'https://{PUBLIC_KEY}:{SECRET_KEY}@sentry.io/{PROJECT_ID}'` | `https://foo:bar@sentry.io/12345` |
| `environment` | [empty] | The environment your application is running in |  `production` |
| `extraTags` | [empty] | `Additional tag names to be extracted from MDC when using SLF4J` | `foo,bar,baz` |
| `ravenFactory` | [empty] | Specify a custom [`RavenFactory`](https://github.com/getsentry/raven-java/blob/master/raven/src/main/java/com/getsentry/raven/RavenFactory.java) class | `com.example.RavenFactory` |
| `release` | [empty] | The release version of your application | `1.0.0` |
| `serverName` | [empty] | Override the server name (rather than looking it up dynamically) | `10.0.0.1` |
| `tags` | [empty] | Additional tags to be sent with errors | `tag1:value1,tag2:value2` |

## Maven Artifacts

This project is available in the [Central Repository](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.tradier%22%20AND%20a%3A%22dropwizard-raven%22). To add it to your project simply add the following dependency to your POM:

```xml
<dependency>
  <groupId>com.tradier</groupId>
  <artifactId>dropwizard-raven</artifactId>
  <version>1.0.0-1</version>
</dependency>
```

## Support

Please file bug reports and feature requests in [GitHub issues](https://github.com/tradier/dropwizard-raven/issues).

## Acknowledgements

Thanks to [gini](https://github.com/gini) for [dropwizard-gelf](https://github.com/gini/dropwizard-gelf) from which much of the original implementation was derived.

## Copyright

Copyright (c) 2014-2016 Tradier Inc. See [LICENSE](LICENSE.md) for detail.
