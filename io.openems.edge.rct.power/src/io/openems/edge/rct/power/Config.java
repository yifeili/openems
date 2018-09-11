package io.openems.edge.rct.power;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition( //
		name = "RCT POWER", //
		description = "Implements a RCT POWER.")
@interface Config {
	String service_pid();

	String id() default "rctPower0";

	@AttributeDefinition(name = "IP-Address", description = "The IP address")
	String ip();

	@AttributeDefinition(name = "Unit ID", description = "The Unit ID")
	int unitID() default 0xff;

	@AttributeDefinition(name = "Port", description = "The Port")
	int port() default 81;

	boolean enabled() default true;

	String webconsole_configurationFactory_nameHint() default "RCT POWER[{id}]";
}