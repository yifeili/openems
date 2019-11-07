package io.openems.edge.project.sambia.controller.supplybusswitch.newVersion;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "Project Sambia Controller Supply-Bus-Switch New Version", //
		description = "")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "ctrlSambiaSupplyBusSwitch0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;

	@AttributeDefinition(name = "Switch-Delay", description = "Delay between ess disconnected and next ess connected.")
	long switchDelay() default 10_000;

	@AttributeDefinition(name = "Supply Bus1 On Indication", description = "Supply Bus1 On Indication Channel.")
	String supplyBus1OnIndication();

	@AttributeDefinition(name = "Supply Bus2 On Indication", description = "Supply Bus2 On Indication Channel.")
	String supplyBus2OnIndication();

	@AttributeDefinition(name = "Bus1 Loads", description = "Bus1 Loads.")
	String[] LoadsBus1() default { "io1/InputOutput1", "io1/InputOutput2", "io1/InputOutput3" };

	@AttributeDefinition(name = "Bus2 Loads", description = "Bus2 Loads.")
	String[] LoadsBus2() default { "io1/InputOutput4", "io1/InputOutput5", "io1/InputOutput6" };

	@AttributeDefinition(name = "Supply-Bus", description = "Collection of the switches for the supplyBus. Each array represents the switches for one supply bus.")
	String supplyBus1Config() default "[{\"Switches\":[{\"ess\": \"ess1\",\"switchAddress\": \"io2/InputOutput0\"},"
			+ "{\"ess\": \"ess1\",\"switchAddress\": \"io2/InputOutput1\"},{\"ess\": \"ess2\",\"switchAddress\": \"io2/InputOutput2\"}]}";

	@AttributeDefinition(name = "Supply-Bus", description = "Collection of the switches for the supplyBus. Each array represents the switches for one supply bus.")
	String supplyBus2Config() default "{\"switches\":[{\"ess\": \"ess1\",\"switchAddress\": \"io2/InputOutput3\"},"
			+ "{\"ess\": \"ess1\",\"switchAddress\": \"io2/InputOutput4\"},{\"ess\": \"ess2\",\"switchAddress\": \"io2/InputOutput5\"}]}]";

	String webconsole_configurationFactory_nameHint() default "Project Sambia Controller Supply-Bus-Switch New Version [{id}]";

}
