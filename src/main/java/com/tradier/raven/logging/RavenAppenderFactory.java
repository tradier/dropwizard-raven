package com.tradier.raven.logging;

import static com.google.common.base.Preconditions.checkNotNull;
import io.dropwizard.logging.AbstractAppenderFactory;
import net.kencochrane.raven.logback.SentryAppender;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;



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

        appender.start();

        Appender<ILoggingEvent> asyncAppender = wrapAsync(appender);
        addThresholdFilter(asyncAppender, threshold);
        addDroppingRavenLoggingFilter(asyncAppender);

        return asyncAppender;
    }

    public void addDroppingRavenLoggingFilter(Appender<ILoggingEvent> appender) {
        Filter<ILoggingEvent> filter = new DroppingRavenLoggingFilter();
        filter.start();
        appender.addFilter(filter);
    }

    public static class DroppingRavenLoggingFilter extends Filter<ILoggingEvent> {

        @Override
        public FilterReply decide(ILoggingEvent event) {
            if (event.getLoggerName().startsWith("net.kencochrane.raven")) {
                return FilterReply.DENY;
            } else {
                return FilterReply.ACCEPT;
            }
        }
    }
}
