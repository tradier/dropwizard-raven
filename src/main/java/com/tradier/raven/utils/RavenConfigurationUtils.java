package com.tradier.raven.utils;

import io.dropwizard.Configuration;
import io.dropwizard.logging.AppenderFactory;
import io.dropwizard.logging.DefaultLoggingFactory;

import java.util.Optional;

import com.tradier.raven.logging.RavenAppenderFactory;

public class RavenConfigurationUtils {

  public static Optional<RavenAppenderFactory> getRavenAppenderFactory(Configuration configuration) {
    final DefaultLoggingFactory loggingFactory =
        (DefaultLoggingFactory) configuration.getLoggingFactory();

    for (AppenderFactory appenderFactory : loggingFactory.getAppenders()) {
      if (appenderFactory instanceof RavenAppenderFactory) {
        return Optional.of((RavenAppenderFactory) appenderFactory);
      }
    }

    return Optional.empty();
  }

}
