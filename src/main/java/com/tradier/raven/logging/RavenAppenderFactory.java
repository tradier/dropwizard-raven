package com.tradier.raven.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Layout;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.AbstractAppenderFactory;
import net.kencochrane.raven.logback.SentryAppender;

import static com.google.common.base.Preconditions.checkNotNull;

@JsonTypeName("raven")
public class RavenAppenderFactory extends AbstractAppenderFactory {

	@JsonProperty
	private String dsn = null;

    @JsonProperty
    private String tags = null;

    public String getDsn() {
		return dsn;
	}

	public void setDsn(String dsn) {
		this.dsn = dsn;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Override
    public Appender<ILoggingEvent> build(LoggerContext context, String applicationName, Layout<ILoggingEvent> layout) {
        checkNotNull(context);

        final SentryAppender appender = new SentryAppender();

        appender.setContext(context);
        appender.setDsn(dsn);
        if(tags != null)
        	appender.setTags(tags);

        addThresholdFilter(appender, threshold);
        appender.start();

        return wrapAsync(appender);
    }
}
