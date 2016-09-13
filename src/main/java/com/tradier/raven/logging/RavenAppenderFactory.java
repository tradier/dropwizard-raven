package com.tradier.raven.logging;

import static com.google.common.base.Preconditions.checkNotNull;
import io.dropwizard.logging.AbstractAppenderFactory;
import io.dropwizard.logging.async.AsyncAppenderFactory;
import io.dropwizard.logging.filter.LevelFilterFactory;
import io.dropwizard.logging.layout.LayoutFactory;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.Filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.getsentry.raven.logback.SentryAppender;
import com.tradier.raven.filters.DroppingRavenLoggingFilter;

@JsonTypeName("raven")
public class RavenAppenderFactory extends AbstractAppenderFactory<ILoggingEvent> {

  private static final String APPENDER_NAME = "dropwizard-raven";

  @NotNull
  @JsonProperty
  private String dsn = null;

  @JsonProperty
  private Optional<String> environment = Optional.empty();

  @JsonProperty
  private Optional<String> extraTags = Optional.empty();

  @JsonProperty
  private Optional<String> ravenFactory = Optional.empty();

  @JsonProperty
  private Optional<String> release = Optional.empty();

  @JsonProperty
  private Optional<String> serverName = Optional.empty();

  @JsonProperty
  private Optional<String> tags = Optional.empty();

  public String getDsn() {
    return dsn;
  }

  public void setDsn(String dsn) {
    this.dsn = dsn;
  }

  public Optional<String> getEnvironment() {
    return environment;
  }

  public void setEnvironment(Optional<String> environment) {
    this.environment = environment;
  }

  public Optional<String> getExtraTags() {
    return extraTags;
  }

  public void setExtraTags(Optional<String> extraTags) {
    this.extraTags = extraTags;
  }

  public Optional<String> getRavenFactory() {
    return ravenFactory;
  }

  public void setRavenFactory(Optional<String> ravenFactory) {
    this.ravenFactory = ravenFactory;
  }

  public Optional<String> getRelease() {
    return release;
  }

  public void setRelease(Optional<String> release) {
    this.release = release;
  }

  public Optional<String> getServerName() {
    return serverName;
  }

  public void setServerName(Optional<String> serverName) {
    this.serverName = serverName;
  }

  public Optional<String> getTags() {
    return tags;
  }

  public void setTags(Optional<String> tags) {
    this.tags = tags;
  }

  @Override
  public Appender<ILoggingEvent> build(LoggerContext context,
      String applicationName,
      LayoutFactory<ILoggingEvent> layoutFactory,
      LevelFilterFactory<ILoggingEvent> levelFilterFactory,
      AsyncAppenderFactory<ILoggingEvent> asyncAppenderFactory) {
    checkNotNull(context);

    final SentryAppender appender = new SentryAppender();
    appender.setName(APPENDER_NAME);
    appender.setContext(context);
    appender.setDsn(dsn);

    environment.ifPresent(appender::setEnvironment);
    extraTags.ifPresent(appender::setExtraTags);
    ravenFactory.ifPresent(appender::setRavenFactory);
    release.ifPresent(appender::setRelease);
    serverName.ifPresent(appender::setServerName);
    tags.ifPresent(appender::setTags);

    appender.addFilter(levelFilterFactory.build(threshold));
    getFilterFactories().stream().forEach(f -> appender.addFilter(f.build()));
    appender.start();

    final Appender<ILoggingEvent> asyncAppender = wrapAsync(appender, asyncAppenderFactory, context);
    addDroppingRavenLoggingFilter(asyncAppender);

    return asyncAppender;
  }

  private void addDroppingRavenLoggingFilter(Appender<ILoggingEvent> appender) {
    final Filter<ILoggingEvent> filter = new DroppingRavenLoggingFilter();
    filter.start();
    appender.addFilter(filter);
  }

}
