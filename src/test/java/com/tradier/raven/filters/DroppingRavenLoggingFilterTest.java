package com.tradier.raven.filters;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

public class DroppingRavenLoggingFilterTest {
  @Test
  public void verifyFilterDeniesSentryLoggers() {
    final DroppingRavenLoggingFilter filter = new DroppingRavenLoggingFilter();
    filter.start();

    final LoggingEvent evt = new LoggingEvent();
    evt.setLoggerName("com.getsentry.raven.logback");
    assertEquals(FilterReply.DENY, filter.decide(evt));
  }
  
  @Test
  public void verifyFilterAllowsNonSentryLoggers() {
    final DroppingRavenLoggingFilter filter = new DroppingRavenLoggingFilter();
    filter.start();

    final LoggingEvent evt = new LoggingEvent();
    evt.setLoggerName("com.tradier.raven");
    assertEquals(FilterReply.NEUTRAL, filter.decide(evt));
  }
}
