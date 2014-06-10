package com.tradier.raven.logging;

import java.io.IOException;

import org.junit.Test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.logging.AsyncAppender;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class RavenAppenderFactoryTest {

    @Test
    public void hasValidDefaults() throws IOException, ConfigurationException {
        final RavenAppenderFactory factory = new RavenAppenderFactory();

        assertThat("default dsn is unset", factory.getDsn(), nullValue());
        assertThat("default additional fields are empty", factory.getTags(), nullValue());
    }
	
    @Test(expected = NullPointerException.class)
    public void buildRavenAppenderShouldFailWithNullContext() {
        new RavenAppenderFactory().build(null, "", null);
    }
	
    @Test
    public void buildRavenAppenderShouldWorkWithValidConfiguration() {
        final RavenAppenderFactory raven = new RavenAppenderFactory();
        final String dsn = "https://user:pass@app.getsentry.com/id";

        Appender<ILoggingEvent> appender = raven.build(new LoggerContext(), dsn, null);

        assertThat(appender, instanceOf(AsyncAppender.class));
    }

}
