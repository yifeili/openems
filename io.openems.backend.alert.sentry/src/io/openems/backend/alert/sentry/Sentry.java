package io.openems.backend.alert.sentry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ops4j.pax.logging.spi.PaxAppender;
import org.ops4j.pax.logging.spi.PaxLevel;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.backend.common.component.AbstractOpenemsBackendComponent;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;

/**
 * Pax-Log-Appender for Sentry.io. The logic is largely copied from
 * SentryAppender:
 * https://github.com/getsentry/sentry-java/blob/master/sentry-logback/src/main/java/io/sentry/logback/SentryAppender.java#L124
 */
@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "Alert.Sentry", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = "org.ops4j.pax.logging.appender.name=Alert.Sentry")
public class Sentry extends AbstractOpenemsBackendComponent implements PaxAppender {

	/**
	 * Name of the {@link Event#extra} property containing the Thread name.
	 */
	public static final String THREAD_NAME = "Sentry-Threadname";

	private final Logger log = LoggerFactory.getLogger(Sentry.class);

	private SentryClient sentry = null;

	public Sentry() {
		super("Alert.Sentry");
	}

	@Activate
	void activate(Config config) {
		this.sentry = SentryClientFactory.sentryClient(config.sentryDSN());
	}

	@Deactivate
	void deactivate() {
		if (this.sentry != null) {
			this.sentry.closeConnection();
		}
	}

	@Override
	public void doAppend(PaxLoggingEvent event) {
		if (this.sentry == null) {
			return;
		}
		EventBuilder eventBuilder = this.createEventBuilder(event);
		this.sentry.sendEvent(eventBuilder);
	}

	/**
	 * Builds an EventBuilder based on the logging event.
	 *
	 * @param event Log generated.
	 * @return EventBuilder containing details provided by the logging system.
	 */
	protected EventBuilder createEventBuilder(PaxLoggingEvent event) {
		EventBuilder eventBuilder = new EventBuilder().withSdkIntegration("logback")
				.withTimestamp(new Date(event.getTimeStamp())) //
				.withMessage(event.getRenderedMessage()) //
				.withLogger(event.getLoggerName()) //
				.withLevel(formatLevel(event.getLevel())) //
				.withExtra(THREAD_NAME, event.getThreadName());

		for (Object key : event.getProperties().keySet()) {
			Object value = event.getProperties().get(key);
			if (this.sentry.getMdcTags().contains(String.valueOf(key))) {
				eventBuilder.withTag(String.valueOf(key), String.valueOf(value));
			} else {
				eventBuilder.withExtra(String.valueOf(key), String.valueOf(value));
			}
		}

		return eventBuilder;
	}

	/**
	 * Transforms a {@link PaxLevel} into an {@link Event.Level}.
	 *
	 * @param paxLevel original level as defined in logback.
	 * @return log level used within sentry.
	 */
	protected Event.Level formatLevel(PaxLevel paxLevel) {
		switch (paxLevel.getSyslogEquivalent()) {
		case 0: // FATAL/OFF
		case 3: // ERROR
			return Event.Level.ERROR;
		case 4: // WARN
			return Event.Level.WARNING;
		case 6: // INFO
			return Event.Level.INFO;
		case 7: // DEBUG/TRACE/ALL
			return Event.Level.DEBUG;
		default:
			this.log.warn("Undefined PaxLevel [" + paxLevel.toString() + "/" + paxLevel.getSyslogEquivalent()
					+ "] . Falling back to [INFO].");
		}
		return Event.Level.INFO;
	}

	/**
	 * Extracts message parameters into a List of Strings.
	 * <p>
	 * null parameters are kept as null.
	 *
	 * @param parameters parameters provided to the logging system.
	 * @return the parameters formatted as Strings in a List.
	 */
	protected static List<String> formatMessageParameters(Object[] parameters) {
		List<String> arguments = new ArrayList<>(parameters.length);
		for (Object argument : parameters) {
			arguments.add((argument != null) ? argument.toString() : null);
		}
		return arguments;
	}
}
