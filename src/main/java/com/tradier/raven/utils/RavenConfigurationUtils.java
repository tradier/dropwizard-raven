package com.tradier.raven.utils;

import com.google.common.base.Optional;
import io.dropwizard.Configuration;
import io.dropwizard.logging.AppenderFactory;
import com.tradier.raven.logging.RavenAppenderFactory;

public class RavenConfigurationUtils {

    public static Optional<RavenAppenderFactory> getRavenAppenderFactory(Configuration configuration) {
        for (AppenderFactory appenderFactory : configuration.getLoggingFactory().getAppenders()) {
            if (appenderFactory instanceof RavenAppenderFactory) {
                return Optional.of((RavenAppenderFactory) appenderFactory);
            }
        }
        return Optional.absent();
    }

}

