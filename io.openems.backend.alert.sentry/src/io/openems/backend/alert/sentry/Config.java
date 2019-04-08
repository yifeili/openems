package io.openems.backend.alert.sentry;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "Alert.Sentry", //
		description = "Provides a connection to sentry.io for alerting.")
@interface Config {

	@AttributeDefinition(name = "Sentry DSN", description = "The Data Source Name (DSN) for sentry.io.")
	String sentryDSN() default "https://public:private@host:port/1";

	// @AttributeDefinition(name = "Port", description = "The port of the REST
	// server.")
//	PaxLevel minLevel() default PaxLevel.;

	String webconsole_configurationFactory_nameHint() default "Alert Sentry";

}