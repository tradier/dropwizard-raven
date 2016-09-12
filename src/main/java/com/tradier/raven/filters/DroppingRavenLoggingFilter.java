package com.tradier.raven.filters;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class DroppingRavenLoggingFilter extends Filter<ILoggingEvent> {
  @Override
  public FilterReply decide(ILoggingEvent event) {
    if (event.getLoggerName().startsWith("com.getsentry.raven")) {
      return FilterReply.DENY;
    } else {
      return FilterReply.NEUTRAL;
    }
  }
}
