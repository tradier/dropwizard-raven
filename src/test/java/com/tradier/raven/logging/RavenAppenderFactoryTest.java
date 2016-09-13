package com.tradier.raven.logging;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.logging.async.AsyncLoggingEventAppenderFactory;
import io.dropwizard.logging.filter.ThresholdLevelFilterFactory;
import io.dropwizard.logging.layout.DropwizardLayoutFactory;

import java.io.IOException;

import org.junit.Test;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

public class RavenAppenderFactoryTest {
  private final LoggerContext context = new LoggerContext();
  private final DropwizardLayoutFactory layoutFactory = new DropwizardLayoutFactory();
  private final ThresholdLevelFilterFactory levelFilterFactory = new ThresholdLevelFilterFactory();
  private final AsyncLoggingEventAppenderFactory asyncAppenderFactory = new AsyncLoggingEventAppenderFactory();

  @Test
  public void hasValidDefaults() throws IOException, ConfigurationException {
    final RavenAppenderFactory factory = new RavenAppenderFactory();

    assertNull("default dsn is unset", factory.getDsn());
    assertFalse("default environment is empty", factory.getEnvironment().isPresent());
    assertFalse("default extraTags is empty", factory.getEnvironment().isPresent());
    assertFalse("default ravenFactory is empty", factory.getRavenFactory().isPresent());
    assertFalse("default release is empty", factory.getRelease().isPresent());
    assertFalse("default serverName is empty", factory.getServerName().isPresent());
    assertFalse("default tags are empty", factory.getTags().isPresent());
  }

  @Test(expected = NullPointerException.class)
  public void buildRavenAppenderShouldFailWithNullContext() {
    new RavenAppenderFactory().build(null, "", null, levelFilterFactory, asyncAppenderFactory);
  }

  @Test
  public void buildRavenAppenderShouldWorkWithValidConfiguration() {
    final RavenAppenderFactory raven = new RavenAppenderFactory();
    final String dsn = "https://user:pass@app.getsentry.com/id";

    Appender<ILoggingEvent> appender =
        raven.build(context, dsn, layoutFactory, levelFilterFactory, asyncAppenderFactory);

    assertThat(appender, instanceOf(AsyncAppender.class));
  }

}
