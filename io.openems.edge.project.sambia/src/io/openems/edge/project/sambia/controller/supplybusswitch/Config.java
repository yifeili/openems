package io.openems.edge.project.sambia.controller.supplybusswitch;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "Project Sambia Controller Supply-Bus-Switch", //
		description = "")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "ctrlSambiaSupplyBusSwitch0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;

	@AttributeDefinition(name = "Ess-IDs", description = "ID of Ess devices.")
	String[] ess_ids();

	@AttributeDefinition(name = "Switch-Delay", description = "Delay between ess disconnected and next ess connected.")
	long switchDelay() default 10_000;

	@AttributeDefinition(name = "Supply-Bus", description = "Collection of the switches for the supplyBus. Each array represents the switches for one supply bus.")
	String supplyBusConfig();

	String webconsole_configurationFactory_nameHint() default "Project Sambia Controller Supply-Bus-Switch [{id}]";

}